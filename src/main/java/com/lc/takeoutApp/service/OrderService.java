package com.lc.takeoutApp.service;

import com.lc.takeoutApp.pojo.Order;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface OrderService {
    Flux<Order> findWaitingDeliveryOrders(int indexStart, int indexEnd);
    Mono<Order> putOrder(Long userId, Long restaurantId, Order order);
    Flux<Order> findUsersDeliveringOrders(Long userId);
    Mono<Boolean> takeOrderByR(Long restaurantId, String orderId);
    Mono<Order> findNotTakenOrder(Long restaurantId, String orderId);
    Flux<Order> findDeliveringOrders(Long userId);
    Flux<Order> findHistoryOrders(Long userId, Pageable pageable);
    Mono<Boolean> rejectOrder(Long restaurantId, String orderId);
    Mono<Order> takeOrderByD(Long userId, String orderId);
    Mono<Order> completeOrder(Long userId, String orderId);
    Mono<List<Order>> findDeliveringOrders(List<String> orderId);
//    Mono<Boolean> cancelOrder(Long userId, String orderId);
}
