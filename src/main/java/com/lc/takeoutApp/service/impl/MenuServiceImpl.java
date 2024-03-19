package com.lc.takeoutApp.service.impl;

import com.lc.takeoutApp.pojo.Restaurant;
import com.lc.takeoutApp.pojo.jsonEntity.Menu;
import com.lc.takeoutApp.repository.MenuRepository;
import com.lc.takeoutApp.repository.RestaurantRepository;
import com.lc.takeoutApp.service.MenuService;
import com.lc.takeoutApp.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.UUID;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    AliOssUtil aliOssUtil;

    private final String DEFAULT_IMAGE_FILENAME = "default_menu_img.png";
//
//    @Override
//    public Flux<Menu> findAllMenuByRestaurantId(Long restaurantId) {
//        return menuRepository.findAllByRestaurantId(restaurantId);
//    }

    @Override
    public Mono<Restaurant> addMenu(Long restaurantId, Menu menu, int categoryIndex) {
        return restaurantRepository.findById(restaurantId)
                .filter(restaurant -> restaurant.getCategories().size() > categoryIndex)
                .doOnNext(restaurant -> menu.setImageFilename(DEFAULT_IMAGE_FILENAME)) //设置默认值
                .doOnNext(restaurant -> restaurant.getCategories().get(categoryIndex).getMenus().add(menu))
                .flatMap(restaurant -> restaurantRepository.save(restaurant));
    }

    @Override
    public Mono<Restaurant> updateMenu(Long restaurantId, Menu menu, int categoryIndex, int menuIndex) {
        return restaurantRepository.findById(restaurantId)
                .filter(restaurant -> restaurant.getCategories().size() > categoryIndex)
                .filter(restaurant -> restaurant.getCategories().get(categoryIndex).getMenus().size() > menuIndex)
                .doOnNext(restaurant -> restaurant.getCategories().get(categoryIndex).getMenus().set(menuIndex, menu))
                .flatMap(restaurant -> restaurantRepository.save(restaurant));
    }

    @Override
    public Mono<Restaurant> deleteMenu(Long restaurantId, int categoryIndex, int menuIndex) {
        return restaurantRepository.findById(restaurantId)
                .filter(restaurant -> restaurant.getCategories().size() > categoryIndex)
                .filter(restaurant -> restaurant.getCategories().get(categoryIndex).getMenus().size() > menuIndex)
                .doOnNext(restaurant -> restaurant.getCategories().get(categoryIndex).getMenus().remove(menuIndex))
                .flatMap(restaurant -> restaurantRepository.save(restaurant));
    }

    @Override
    public Mono<String> uploadImage(Long restaurantId, int categoryIndex, int menuIndex, String filename, InputStream inputStream) {
        String filePostfix = filename.substring(filename.lastIndexOf("."));
        String tmpFilename;
        do {
            //保证文件名不重复
            tmpFilename = UUID.randomUUID().toString().replaceAll("-", "") + filePostfix;
        } while (aliOssUtil.objectExist(tmpFilename));
        String newFilename = tmpFilename;
        return restaurantRepository.findById(restaurantId)
                .flatMap(restaurant -> {
                    boolean uploadResult = aliOssUtil.upload(newFilename, inputStream);
                    if(uploadResult){
                        //如果之前不是默认文件，删除之前的文件
                        if(!restaurant.getCategories().get(categoryIndex).getMenus().get(menuIndex).getImageFilename().equals(DEFAULT_IMAGE_FILENAME)){
                            aliOssUtil.delete(restaurant.getCategories().get(categoryIndex).getMenus().get(menuIndex).getImageFilename());
                        }
                        restaurant.getCategories().get(categoryIndex).getMenus().get(menuIndex).setImageFilename(newFilename);
                        return restaurantRepository.save(restaurant).thenReturn(newFilename);
                    }
                    else {
                        return Mono.empty();
                    }
                });
    }
}
