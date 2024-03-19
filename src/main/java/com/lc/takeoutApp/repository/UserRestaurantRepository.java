package com.lc.takeoutApp.repository;

import com.lc.takeoutApp.pojo.UserRestaurant;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRestaurantRepository extends R2dbcRepository<UserRestaurant, Long> {
    Mono<UserRestaurant> findByUserId(Long userId);
}
