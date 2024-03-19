package com.lc.takeoutApp.service;

import com.lc.takeoutApp.pojo.RestaurantComment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RestaurantCommentService {
    Flux<RestaurantComment> getComments(Long restaurantId);
    Mono<RestaurantComment> addComment(Long userId, String orderId, RestaurantComment comment);
}
