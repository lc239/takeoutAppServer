package com.lc.takeoutApp.service;

import com.lc.takeoutApp.pojo.Restaurant;
import com.lc.takeoutApp.pojo.jsonEntity.Menu;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;

public interface MenuService {
//    Flux<Menu> findAllMenuByRestaurantId(Long restaurantId);
    Mono<Restaurant> addMenu(Long restaurantId, Menu menu, int categoryIndex);
    Mono<Restaurant> updateMenu(Long restaurantId, Menu menu, int categoryIndex, int menuIndex);
    Mono<Restaurant> deleteMenu(Long restaurantId, int categoryIndex, int menuIndex);
    Mono<String> uploadImage(Long restaurantId, int categoryIndex, int menuIndex, String filename, InputStream inputStream);
}
