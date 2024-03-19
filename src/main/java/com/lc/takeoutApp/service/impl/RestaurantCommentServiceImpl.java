package com.lc.takeoutApp.service.impl;

import com.lc.takeoutApp.pojo.RestaurantComment;
import com.lc.takeoutApp.repository.RestaurantCommentRepository;
import com.lc.takeoutApp.service.RestaurantCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RestaurantCommentServiceImpl implements RestaurantCommentService {

    @Autowired
    RestaurantCommentRepository commentRepository;

    @Override
    public Flux<RestaurantComment> getComments(Long restaurantId) {
        return null;
    }

    @Override
    public Mono<RestaurantComment> addComment(Long userId, String orderId, RestaurantComment comment) {
        return commentRepository.existsByOrderId(orderId)
                .filter(exists -> {
                    if(!exists) return comment.getUserId().equals(userId);
                    return false;
                }).flatMap(aBoolean -> commentRepository.save(comment));
    }
}
