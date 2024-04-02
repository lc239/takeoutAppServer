package com.lc.takeoutApp.service.impl;

import com.lc.takeoutApp.pojo.DeliveryMan;
import com.lc.takeoutApp.pojo.Order;
import com.lc.takeoutApp.repository.DeliveryManRepository;
import com.lc.takeoutApp.service.DeliveryService;
import com.lc.takeoutApp.service.OrderService;
import com.lc.takeoutApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired
    UserService userService;

    @Autowired
    DeliveryManRepository deliveryManRepository;

    @Autowired
    OrderService orderService;

    @Override
    public Mono<DeliveryMan> findByUserId(Long userId) {
        return deliveryManRepository.findByUserId(userId);
    }

    @Override
    public Mono<DeliveryMan> register(Long userId) {
        return userService.changeRole(userId, false, true)
                .flatMap(user -> deliveryManRepository.save(new DeliveryMan(null, userId, 0L)));
    }

    @Override
    public Mono<DeliveryMan> completeOrder(Long userId, String orderId) {
        return orderService.completeOrder(userId, orderId)
                .flatMap(order -> deliveryManRepository.findByUserId(userId))
                .doOnNext(DeliveryMan::completeOne)
                .flatMap(deliveryMan -> deliveryManRepository.save(deliveryMan));
    }
}
