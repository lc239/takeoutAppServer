package com.lc.takeoutApp.service.impl;

import com.lc.takeoutApp.pojo.RestaurantComment;
import com.lc.takeoutApp.repository.OrderRepository;
import com.lc.takeoutApp.repository.RestaurantCommentRepository;
import com.lc.takeoutApp.repository.UserRepository;
import com.lc.takeoutApp.service.RestaurantCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Objects;

@Service
public class RestaurantCommentServiceImpl implements RestaurantCommentService {

    @Autowired
    RestaurantCommentRepository commentRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Flux<RestaurantComment> getComments(Long restaurantId, Pageable pageable) {
        return commentRepository.findAllByRestaurantId(restaurantId, pageable);
    }

    @Override
    public Mono<RestaurantComment> addComment(String orderId, RestaurantComment comment) {
        return orderRepository.findByOrderId(orderId)
                .filter(order -> order.getCommentId() == null)
                .filter(order -> order.getUserId().equals(comment.getUserId()))
                .doOnNext(order -> comment.setRestaurantId(order.getRestaurantId()))
                .doOnNext(order -> comment.setCreateTime(Instant.now()))
                .flatMap(order -> userRepository.findById(order.getUserId())
                        .doOnNext(user -> comment.setUsername(user.getUsername()))
                        .flatMap(user -> commentRepository.save(comment)
                                .doOnNext(comment1 -> order.setCommentId(comment1.getId()))
                                .flatMap(comment1 -> orderRepository.save(order))
                        )
                )
//                .flatMap(order -> restaurantRepository.findById(order.getRestaurantId())) //给店铺的数据更新，数据库触发器做了
//                .doOnNext(restaurant -> restaurant.addComment(comment.getRate()))
//                .flatMap(restaurant -> restaurantRepository.save(restaurant))
                .thenReturn(comment);
    }
}
