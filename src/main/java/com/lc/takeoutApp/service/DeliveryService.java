package com.lc.takeoutApp.service;

import com.lc.takeoutApp.pojo.DeliveryMan;
import reactor.core.publisher.Mono;

public interface DeliveryService {
    Mono<DeliveryMan> findByUserId(Long userId);
    Mono<DeliveryMan> register(Long userId);
    Mono<DeliveryMan> completeOrder(Long userId, String orderId);
}
