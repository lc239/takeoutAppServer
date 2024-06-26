package com.lc.takeoutApp.service.impl;

import com.google.gson.Gson;
import com.lc.takeoutApp.pojo.Restaurant;
import com.lc.takeoutApp.pojo.jsonEntity.Menu;
import com.lc.takeoutApp.pojo.Order;
import com.lc.takeoutApp.pojo.jsonEntity.OrderedMenu;
import com.lc.takeoutApp.pojo.jsonEntity.WebSocketResponse;
import com.lc.takeoutApp.repository.*;
import com.lc.takeoutApp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    Gson gson;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    Map<Long, WebSocketSession> wsMap;

    @Autowired
    ReactiveStringRedisTemplate redisTemplate;

    @Autowired
    UserRestaurantRepository userRestaurantRepository;

    private final String redisListKey = "waitingDeliveryOrders";

    @Override
    public Flux<Order> findWaitingDeliveryOrders(int indexStart, int indexEnd) {
        ReactiveValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        ReactiveListOperations<String, String> opsForList = redisTemplate.opsForList();
        return opsForList.range(redisListKey, indexStart, indexEnd)
                .flatMap(opsForValue::get)
                .map(s -> gson.fromJson(s, Order.class));
    }

    @Override
    public Mono<Order> putOrder(Long userId, Long restaurantId, Order order) {
        order.setTaken(false);
        order.setUserId(userId);
        order.setRestaurantId(restaurantId);
        order.setCreateTime(Instant.now());
        order.setOrderId(UUID.randomUUID().toString().replaceAll("-", ""));
        order.setPackPrice(0); //此处未设置
        return checkOrder(order).flatMap(aBoolean -> {
            if(aBoolean) {
                long price = 0L;
                for(OrderedMenu menu: order.getMenus()){
                    price += menu.getPrice() * menu.getNum();
                }
                order.setPrice(price);
                //通知商家有订单
                userRestaurantRepository.findByRestaurantId(restaurantId)
                        .map(userRestaurant -> wsMap.get(userRestaurant.getUserId()))
                        .filter(Objects::nonNull)
                        .flatMap(webSocketSession -> webSocketSession.send(Mono.just(webSocketSession.textMessage(gson.toJson(new WebSocketResponse<>(0, order.getCreateTime(), order.getOrderId()))))))
                        .subscribe();
                redisTemplate.opsForList().rightPush("user:delivering:" + userId, order.getOrderId()).subscribe(); //添加到用户的配送单列表中
                return redisTemplate.opsForValue().set(order.getOrderId(), gson.toJson(order), Duration.ofMinutes(10)) //等10分钟接单
                        .flatMap(aBoolean1 -> {
                            if(aBoolean1) return Mono.just(order);
                            else return Mono.empty();
                        });
            } else {
                return Mono.empty();
            }
        });
    }

    @Override
    public Flux<Order> findUsersDeliveringOrders(Long userId) {
        return redisTemplate.opsForList().range("user:delivering:" + userId, 0L, -1L)
                .flatMap(orderId -> redisTemplate.opsForValue().get(orderId)
                        .switchIfEmpty(redisTemplate.opsForList().delete(orderId).then(Mono.empty())) //没有就删除并且不返回
                )
                .map(s -> gson.fromJson(s, Order.class));
    }

    @Override
    public Mono<Boolean> takeOrderByR(Long restaurantId, String orderId) {
        ReactiveValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        ReactiveListOperations<String, String> opsForList = redisTemplate.opsForList();
        return opsForValue.get(orderId)
                .map(s -> gson.fromJson(s, Order.class))
                .filter(order -> restaurantId.equals(order.getRestaurantId()))
                .doOnNext(order -> order.setTaken(true))
                .flatMap(order -> opsForList.rightPush(redisListKey, orderId) //存入待接单列表
                        .doOnNext(l -> opsForValue.set(orderId, gson.toJson(order)).subscribe()) //修改状态
                        .doOnNext(l -> { //发送消息
                            WebSocketSession webSocketSession = wsMap.get(order.getUserId());
                            if(webSocketSession != null){
                                webSocketSession.send(Mono.just(webSocketSession.textMessage(gson.toJson(WebSocketResponse.takeOrderByRResponse(orderId))))).subscribe();
                            }
                        }))
                .map(l -> true)
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Order> findNotTakenOrder(Long restaurantId, String orderId) {
        return redisTemplate.opsForValue().get(orderId)
                .map(s -> gson.fromJson(s, Order.class))
                .filter(order -> restaurantId.equals(order.getRestaurantId()))
                .filter(order -> !order.getTaken());
    }

    @Override
    public Flux<Order> findDeliveringOrders(Long deliveryId) {
        return redisTemplate.opsForList().range(deliveryId.toString(), 0L, -1L)
                .flatMap(orderId -> redisTemplate.opsForValue().get(orderId))
                .map(s -> gson.fromJson(s, Order.class));
    }

    @Override
    public Flux<Order> findHistoryOrders(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Mono<Boolean> rejectOrder(Long restaurantId, String orderId) {
        return redisTemplate.opsForValue().get(orderId)
                .map(s -> gson.fromJson(s, Order.class))
                .filter(order -> restaurantId.equals(order.getRestaurantId()))
                .map(order -> wsMap.get(order.getUserId()))
                .filter(Objects::nonNull)
                .doOnNext(webSocketSession -> webSocketSession.send(Mono.just(webSocketSession.textMessage(gson.toJson(WebSocketResponse.rejectOrderResponse())))).subscribe())
                .flatMap(ws -> redisTemplate.opsForValue().delete(orderId))
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Order> takeOrderByD(Long deliveryId, String orderId) {
        ReactiveListOperations<String, String> opsForList = redisTemplate.opsForList();
        ReactiveValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        return opsForList.remove(redisListKey, 1, orderId) //从待接单移除
                .filter(aLong -> aLong.equals(1L))
                .flatMap(aLong -> opsForList.rightPush(deliveryId.toString(), orderId)) //添加到骑手接单列表
                .flatMap(along -> opsForValue.get(orderId))
                .map(s -> gson.fromJson(s, Order.class))
                .doOnNext(order -> order.setDeliveryManId(deliveryId))
                .flatMap(order -> opsForValue.set(orderId, gson.toJson(order)).thenReturn(order));
    }

    //存入订单后有数据库触发器加销量1
    @Override
    public Mono<Order> completeOrder(Long deliveryId, String orderId) {
        ReactiveListOperations<String, String> opsForList = redisTemplate.opsForList();
        ReactiveValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        return opsForList.remove(deliveryId.toString(), 1, orderId)
                .filter(aLong -> aLong.equals(1L))
                .flatMap(aLong -> opsForValue.get(orderId))
                .map(s -> gson.fromJson(s, Order.class))
                .flatMap(order -> {
                    WebSocketSession session = wsMap.get(order.getUserId());
                    if(session != null){
                        session.send(Mono.just(session.textMessage(gson.toJson(WebSocketResponse.completeOrderResponse(orderId))))).subscribe(); //给用户通知
                    }
                    order.setCompleteTime(Instant.now());
                    return orderRepository.save(order); //完成后保存到数据库
                })
                .doOnNext(order -> opsForValue.delete(orderId).subscribe());
    }

    @Override
    public Mono<List<Order>> findDeliveringOrders(List<String> orderId) {
        return redisTemplate.opsForValue().multiGet(orderId)
                .map(strings -> strings.stream().map(s -> gson.fromJson(s, Order.class)).collect(Collectors.toList()));
    }

//
//    @Override
//    public Mono<Boolean> cancelOrder(Long userId, String orderId) {
//        return redisTemplate.opsForValue().get(orderId)
//                .map(s -> new Gson().fromJson(s, Order.class))
//                .filter(order -> order.getUserId().equals(userId)) //订单里用户id一致
//                .flatMap(order -> redisTemplate.delete(orderId))
//                .map(aLong -> aLong.equals(1L))
//                .defaultIfEmpty(false);
//    }

    //检查是否是一个参数正常的order
    private Mono<Boolean> checkOrder(Order order){
        Long restaurantId = order.getRestaurantId();
        return userRestaurantRepository.findByRestaurantId(restaurantId)
                .filter(userRestaurant -> wsMap.containsKey(userRestaurant.getUserId())) //检查是否在线
                .flatMap(userRestaurant -> restaurantRepository.findById(restaurantId))
                .doOnNext(restaurant -> order.setDeliveryPrice(restaurant.getDeliveryPrice())) //此处设置配送费
                .map(Restaurant::getCategories)
                .filter(categories -> order.getMenus().stream().allMatch(orderedMenu -> {
                    ArrayList<Menu> menus = categories.get(orderedMenu.getCategoryIndex()).getMenus();
                    for (Menu menu : menus) {
                        if (menu.getName().equals(orderedMenu.getName()) && menu.getPrice().equals(orderedMenu.getPrice())) {
                            return true;
                        }
                    }
                    return false;
                })).hasElement();
    }
}
