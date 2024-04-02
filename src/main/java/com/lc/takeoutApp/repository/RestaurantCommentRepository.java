package com.lc.takeoutApp.repository;

import com.lc.takeoutApp.pojo.RestaurantComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RestaurantCommentRepository extends R2dbcRepository<RestaurantComment, Long> {
    Flux<RestaurantComment> findAllByRestaurantId(Long restaurantId, Pageable pageable);
}
