package com.lc.takeoutApp.repository;

import com.lc.takeoutApp.pojo.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrderRepository extends R2dbcRepository<Order, String> {
//    Flux<Order> find
    Flux<Order> findAllByUserId(Long userId);
    Flux<Order> findAllByUserId(Long userId, Pageable pageable);
}
