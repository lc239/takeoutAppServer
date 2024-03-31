package com.lc.takeoutApp.service.impl;

import com.lc.takeoutApp.pojo.DeliveryMan;
import com.lc.takeoutApp.repository.DeliveryManRepository;
import com.lc.takeoutApp.service.DeliveryService;
import com.lc.takeoutApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired
    UserService userService;

    @Autowired
    DeliveryManRepository deliveryManRepository;

    @Override
    public Mono<DeliveryMan> findByUserId(Long userId) {
        return deliveryManRepository.findByUserId(userId);
    }

    @Override
    public Mono<DeliveryMan> register(Long userId) {
        return userService.changeRole(userId, false, true)
                .flatMap(user -> deliveryManRepository.save(new DeliveryMan(null, userId, new ArrayList<>(), 0L)));
    }
}
