package com.lc.takeoutApp.service.impl;

import com.google.gson.Gson;
import com.lc.takeoutApp.pojo.jsonEntity.Menu;
import com.lc.takeoutApp.pojo.Order;
import com.lc.takeoutApp.pojo.jsonEntity.OrderedMenu;
import com.lc.takeoutApp.pojo.User;
import com.lc.takeoutApp.repository.*;
import com.lc.takeoutApp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashSet;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

//    @Autowired
//    OrderRepository orderRepository;
//
//    @Autowired
//    MenuRepository menuRepository;
//
//    @Autowired
//    RestaurantRepository restaurantRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    ReactiveStringRedisTemplate redisTemplate;
//
//    @Override
//    public Flux<Order> findOrders(Long userId, Pageable pageable) {
//        return orderRepository.findAllByUserId(userId, pageable);
//    }

//    @Override
//    public Mono<Boolean> putOrder(Long userId, Order order) {
//        order.setId(UUID.randomUUID().toString().replaceAll("-", "")); //uuid加上单个用户的id两个字段确定一个订单，单用户生成一个订单号可能性可忽略
//        //TODO 这里传参数
//        order.setPackPrice(1);
//        order.setDeliveryPrice(1);
//        order.setPrice(100L);
//        order.setAddress("默认地址");
//        return checkOrder(userId, order).flatMap(aBoolean -> {
//            if(aBoolean) return redisTemplate.opsForValue().set(order.getId(), new Gson().toJson(order), Duration.ofMinutes(10)); //等10分钟接单
//            else return Mono.just(false);
//        });
//    }

//    @Override
//    public Mono<Boolean> takeOrderByR(Long restaurantId, String orderId) {
//        ReactiveValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
//        return opsForValue.get(orderId)
//                .map(s -> new Gson().fromJson(s, Order.class))
//                .filter(order -> restaurantId.equals(order.getRestaurantId()))
//                .doOnNext(order -> {
//                    orderRepository.save(order).subscribe(); //订单添加到mysql
//                })
//                .flatMap(order -> opsForValue.delete(order.getId())) //订单从redis删除
//                .defaultIfEmpty(false);
//    }
//
//    @Override
//    public Mono<Order> takeOrderByD(Long userId, String orderId) {
//        return userRepository.findById(userId)
//                .filter(User::getIsDeliveryMan) //检查是不是骑手
//                .flatMap(user -> orderRepository.findById(orderId))
//                .doOnNext(order1 -> order1.setDeliveryManId(userId)) //设置接单骑手字段
//                .flatMap(order1 -> orderRepository.save(order1));
//    }
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

//    //检查是否是一个参数正常的order
//    private Mono<Boolean> checkOrder(Long userId, Order order){
//        if(!userId.equals(order.getUserId())) return Mono.empty(); //检查用户id是否一致
//        Long restaurantId = order.getRestaurantId();
//        return restaurantRepository.findById(restaurantId)
//                .filter(restaurant -> restaurant.getId().equals(restaurantId)) //检查店铺id是否正确
//                .flatMapMany(restaurant -> menuRepository.findAllByRestaurantId(restaurantId))
//                .map(Menu::getId)
//                .collectList()
//                .map(menuIdList -> new HashSet<>(menuIdList).containsAll(order.getMenus().stream().map(OrderedMenu::getId).toList())); //检查菜的id是不是都在店铺中存在
//    }
}
