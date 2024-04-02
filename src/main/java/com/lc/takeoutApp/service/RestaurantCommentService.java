package com.lc.takeoutApp.service;

import com.lc.takeoutApp.pojo.RestaurantComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RestaurantCommentService {
    Flux<RestaurantComment> getComments(Long restaurantId, Pageable pageable);
    Mono<RestaurantComment> addComment(String orderId, RestaurantComment comment);
}
