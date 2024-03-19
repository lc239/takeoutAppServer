package com.lc.takeoutApp.service.impl;

import com.lc.takeoutApp.pojo.Restaurant;
import com.lc.takeoutApp.pojo.UserRestaurant;
import com.lc.takeoutApp.pojo.jsonEntity.Category;
import com.lc.takeoutApp.repository.MenuRepository;
import com.lc.takeoutApp.repository.RestaurantRepository;
import com.lc.takeoutApp.repository.UserRestaurantRepository;
import com.lc.takeoutApp.service.RestaurantService;
import com.lc.takeoutApp.service.UserService;
import com.lc.takeoutApp.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    UserService userService;

    @Autowired
    UserRestaurantRepository userRestaurantRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    AliOssUtil aliOssUtil;

    private final String DEFAULT_IMAGE_FILENAME = "default_restaurant_img.png";

    @Override
    public Mono<Restaurant> findByRestaurantId(Long restaurantId) {
        return restaurantRepository.findById(restaurantId);
    }

    @Override
    public Flux<Restaurant> findAllBy(Pageable pageable) {
        return restaurantRepository.findAllBy(pageable);
    }

    @Override
    public Mono<Restaurant> register(Long userId, Restaurant restaurant) {
        return userRestaurantRepository.findByUserId(userId).hasElement().flatMap(aBoolean -> {
            if(aBoolean){
                return Mono.empty();
            }
            else {
                restaurant.setCategories(new ArrayList<>()); //初始化
                return restaurantRepository.save(restaurant)
                        .doOnNext(restaurant1 -> userService.changeRole(userId, true, true).subscribe()) //用户变成商家
                        .doOnNext(savedRestaurant -> userRestaurantRepository.save(new UserRestaurant(null, userId, savedRestaurant.getId())).subscribe()); //保存用户店铺关系
            }
        });
    }

    @Override
    public Mono<Restaurant> addCategory(Long restaurantId, String name) {
        return restaurantRepository.findById(restaurantId)
                .flatMap(restaurant -> {
                    //最大保存10个分类
                    if(restaurant.getCategories().size() >= 10){
                        return Mono.empty();
                    }
                    Category newCategory = new Category();
                    newCategory.setName(name);
                    newCategory.setMenus(new ArrayList<>());
                    restaurant.getCategories().add(newCategory);
                    return restaurantRepository.save(restaurant);
                });
    }

    @Override
    public Mono<Restaurant> deleteCategory(Long restaurantId, int index) {
        return restaurantRepository.findById(restaurantId)
                .flatMap(restaurant -> {
                    if(restaurant.getCategories().size() <= index){
                        return Mono.empty();
                    }
                    restaurant.getCategories().remove(index);
                    return restaurantRepository.save(restaurant);
                });
    }

    @Override
    public Mono<Restaurant> updateCategoryName(Long restaurantId, int index, String name) {
        return restaurantRepository.findById(restaurantId)
                .flatMap(restaurant -> {
                    System.out.println(restaurant);
                    System.out.println(restaurant.getCategories().get(0));
                    System.out.println(restaurant.getCategories().get(0).getName());
                    if(restaurant.getCategories().size() <= index || restaurant.getCategories().get(index).getName().equals(name)){
                        return Mono.empty();
                    }
                    restaurant.getCategories().get(index).setName(name);
                    return restaurantRepository.save(restaurant);
                });
    }

    @Override
    public Mono<String> uploadImage(Long restaurantId, String filename, InputStream inputStream) {
        String filePostfix = filename.substring(filename.lastIndexOf("."));
        String tmpFilename;
        do {
            tmpFilename = UUID.randomUUID().toString().replaceAll("-", "") + filePostfix;
        } while (aliOssUtil.objectExist(tmpFilename));
        String newFilename = tmpFilename;
        return restaurantRepository.findById(restaurantId)
                .flatMap(restaurant -> {
                    boolean uploadResult = aliOssUtil.upload(newFilename, inputStream);
                    if(uploadResult){
                        //如果之前不是默认头像，删除之前的文件
                        if(!restaurant.getImageFilename().equals(DEFAULT_IMAGE_FILENAME)){
                            aliOssUtil.delete(restaurant.getImageFilename());
                        }
                        restaurant.setImageFilename(newFilename);
                        return restaurantRepository.save(restaurant).thenReturn(newFilename);
                    }
                    else return Mono.empty();
                });
    }
}
