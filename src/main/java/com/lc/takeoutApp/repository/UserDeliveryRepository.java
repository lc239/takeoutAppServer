package com.lc.takeoutApp.repository;

import com.lc.takeoutApp.pojo.UserDelivery;
import com.lc.takeoutApp.pojo.UserRestaurant;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserDeliveryRepository extends R2dbcRepository<UserDelivery, Long> {
    Mono<UserDelivery> findByUserId(Long userId);
}
