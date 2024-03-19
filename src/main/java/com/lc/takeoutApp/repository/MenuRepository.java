package com.lc.takeoutApp.repository;

import com.lc.takeoutApp.pojo.jsonEntity.Menu;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@Repository
public interface MenuRepository extends R2dbcRepository<Menu, Long> {
//    Flux<Menu> findAllByRestaurantId(Long restaurantId);
//    Mono<Void> deleteAllByRestaurantIdAndCategoryIndex(Long restaurantId, Integer categoryIndex);
}
