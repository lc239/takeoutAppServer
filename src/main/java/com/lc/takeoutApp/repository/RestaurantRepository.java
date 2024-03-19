package com.lc.takeoutApp.repository;

import com.lc.takeoutApp.pojo.Restaurant;
import com.lc.takeoutApp.pojo.RestaurantComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RestaurantRepository extends R2dbcRepository<Restaurant, Long> {
    Flux<Restaurant> findAllBy(Pageable pageable);
}
