package com.lc.takeoutApp.service;

import com.lc.takeoutApp.pojo.Restaurant;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;

public interface RestaurantService {
    Mono<Restaurant> findByRestaurantId(Long restaurantId);
    Flux<Restaurant> findAllBy(Pageable pageable);
    Flux<Restaurant> findAllByPrefix(String prefix, int size);
    Mono<Restaurant> register(Long userId, Restaurant restaurant);
    Mono<Restaurant> addCategory(Long restaurantId, String name);
    Mono<Restaurant> deleteCategory(Long restaurantId, int index);
    Mono<Restaurant> updateCategoryName(Long restaurantId, int index, String name);
    Mono<String> uploadImage(Long restaurantId, String filename, InputStream inputStream);
}
