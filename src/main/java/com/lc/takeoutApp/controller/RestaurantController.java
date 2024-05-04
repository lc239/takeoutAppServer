package com.lc.takeoutApp.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.lc.takeoutApp.pojo.CommonResponse;
import com.lc.takeoutApp.pojo.Order;
import com.lc.takeoutApp.pojo.Restaurant;
import com.lc.takeoutApp.pojo.RestaurantComment;
import com.lc.takeoutApp.pojo.jsonEntity.Menu;
import com.lc.takeoutApp.service.MenuService;
import com.lc.takeoutApp.service.OrderService;
import com.lc.takeoutApp.service.RestaurantCommentService;
import com.lc.takeoutApp.service.RestaurantService;
import com.lc.takeoutApp.view.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    MenuService menuService;

    @Autowired
    OrderService orderService;

    @Autowired
    RestaurantCommentService commentService;

    @PostMapping("/register")
    Mono<CommonResponse<Restaurant>> register(@RequestHeader("userId") Long userId, @RequestBody Restaurant restaurant){
        return restaurantService.register(userId, restaurant)
                .map(CommonResponse::success)
                .defaultIfEmpty(CommonResponse.error("注册失败，请换名重试"));
    }

    @GetMapping("/info")
    Mono<CommonResponse<Restaurant>> getRestaurant(@RequestHeader("restaurantId") Long restaurantId){
        return restaurantService.findByRestaurantId(restaurantId)
                .map(CommonResponse::success)
                .defaultIfEmpty(CommonResponse.error("查询不存在"));
    }

    @GetMapping("/info/{restaurantId}")
    Mono<CommonResponse<Restaurant>> getRestaurantById(@PathVariable("restaurantId") Long restaurantId){
        return restaurantService.findByRestaurantId(restaurantId)
                .map(CommonResponse::success)
                .defaultIfEmpty(CommonResponse.error("查询不存在"));
    }

    //此方法获取的是预览
    @JsonView(View.RestaurantPreviewView.class)
    @GetMapping("/info/{pageOffset}/{pageSize}")
    Mono<CommonResponse<List<Restaurant>>> getRestaurants(@PathVariable("pageOffset") int pageOffset, @PathVariable("pageSize") int pageSize){
        return restaurantService.findAllBy(PageRequest.of(pageOffset, pageSize))
                .collectList()
                .map(CommonResponse::success);
    }

    @JsonView(View.RestaurantSearchView.class)
    @GetMapping("/search/{size}/{prefix}")
    Mono<CommonResponse<List<Restaurant>>> searchRestaurants(@PathVariable("size") int size, @PathVariable("prefix") String prefix){
        return restaurantService.findAllByPrefix(prefix, size)
                .collectList()
                .map(restaurants -> CommonResponse.success(restaurants))
                .defaultIfEmpty(CommonResponse.error("未查询到结果"));
    }

    @GetMapping("/comment/{restaurantId}/{pageOffset}/{pageSize}")
    Mono<CommonResponse<List<RestaurantComment>>> getComments(
            @PathVariable("restaurantId") Long restaurantId,
            @PathVariable("pageOffset") int pageOffset,
            @PathVariable("pageSize") int pageSize
    ){
        return commentService.getComments(restaurantId, PageRequest.of(pageOffset, pageSize))
                .collectList()
                .map(CommonResponse::success);
    }

    @PatchMapping("/category/add/{name}")
    Mono<CommonResponse<Void>> addCategory(@RequestHeader("restaurantId") Long restaurantId, @PathVariable("name") String name){
        return restaurantService.addCategory(restaurantId, name)
                .map(restaurant -> CommonResponse.success())
                .defaultIfEmpty(CommonResponse.error("请检查分类添加规则"));
    }

    @PatchMapping("/category/delete/{index}")
    Mono<CommonResponse<Void>> deleteCategory(@RequestHeader("restaurantId") Long restaurantId, @PathVariable("index") Integer index){
        return restaurantService.deleteCategory(restaurantId, index)
                .map(restaurant -> CommonResponse.success())
                .defaultIfEmpty(CommonResponse.error("删除失败，请检查参数"));
    }

    @PatchMapping("/category/update/{index}/{name}")
    Mono<CommonResponse<Restaurant>> updateCategoryName(
            @RequestHeader("restaurantId") Long restaurantId,
            @PathVariable("index") Integer index,
            @PathVariable("name") String name
    ){
        return restaurantService.updateCategoryName(restaurantId, index, name)
                .map(restaurant -> CommonResponse.success(restaurant))
                .defaultIfEmpty(CommonResponse.error("修改失败，请检查分类规则"));
    }

    @PutMapping("/upload/image")
    Mono<CommonResponse<String>> uploadImage(
            @RequestHeader("restaurantId") Long restaurantId,
            @RequestPart("image") FilePart imagePart
    ){
        return DataBufferUtils.join(imagePart.content())
                .flatMap(dataBuffer -> restaurantService.uploadImage(restaurantId, imagePart.filename(), dataBuffer.asInputStream()))
                .map(s -> CommonResponse.success(s))
                .defaultIfEmpty(CommonResponse.error("参数错误"));
    }

    @PutMapping("/order/take/{orderId}")
    Mono<CommonResponse> takeOrder(@RequestHeader("restaurantId") Long restaurantId, @PathVariable("orderId") String orderId){
        return orderService.takeOrderByR(restaurantId, orderId).map(aBoolean -> {
            if(aBoolean) {
                return CommonResponse.success();
            } else {
                return CommonResponse.error("接单失败，可能已取消");
            }
        });
    }

    @DeleteMapping("/order/delete/{orderId}")
    Mono<CommonResponse<Void>> rejectOrder(@RequestHeader("restaurantId") Long restaurantId, @PathVariable("orderId") String orderId){
        return orderService.rejectOrder(restaurantId, orderId).thenReturn(CommonResponse.success());
    }

    @GetMapping("/order/take/{orderId}")
    Mono<CommonResponse<Order>> getNotTakenOrder(@RequestHeader("restaurantId") Long restaurantId, @PathVariable("orderId") String orderId){
        return orderService.findNotTakenOrder(restaurantId, orderId)
                .map(CommonResponse::success)
                .defaultIfEmpty(CommonResponse.error("未查询到该订单"));
    }

    @PatchMapping("/menu/add/{index}")
    Mono<CommonResponse<Menu>> add(
            @RequestHeader("restaurantId") Long restaurantId,
            @RequestBody Menu menu,
            @PathVariable("index") int categoryIndex
    ){
        return menuService.addMenu(restaurantId, menu, categoryIndex)
                .map(restaurant -> CommonResponse.success(
                            restaurant.getCategories().get(categoryIndex).getMenus().get(restaurant.getCategories().get(categoryIndex).getMenus().size() - 1)
                    ))
                .defaultIfEmpty(CommonResponse.error("添加失败，请检查菜品规则"));
    }

    @PatchMapping("/menu/update/{categoryIndex}/{menuIndex}")
    Mono<CommonResponse<Void>> update(
            @RequestHeader("restaurantId") Long restaurantId,
            @RequestBody Menu menu,
            @PathVariable("categoryIndex") int categoryIndex,
            @PathVariable("menuIndex") int menuIndex
    ){
        return menuService.updateMenu(restaurantId, menu, categoryIndex, menuIndex)
                .map(restaurant -> CommonResponse.success())
                .defaultIfEmpty(CommonResponse.error("修改失败，请检查菜品规则"));
    }

    @PatchMapping("/menu/delete/{categoryIndex}/{menuIndex}")
    Mono<CommonResponse> delete(
            @RequestHeader("restaurantId") Long restaurantId,
            @PathVariable("categoryIndex") int categoryIndex,
            @PathVariable("menuIndex") int menuIndex
    ){
        return menuService.deleteMenu(restaurantId, categoryIndex, menuIndex).thenReturn(CommonResponse.success());
    }

    @PutMapping("/menu/upload/image/{categoryIndex}/{menuIndex}")
    Mono<CommonResponse<String>> uploadMenuImage(
            @RequestHeader("restaurantId") Long restaurantId,
            @PathVariable("categoryIndex") int categoryIndex,
            @PathVariable("menuIndex") int menuIndex,
            @RequestPart("image") FilePart imagePart
    ){
        return DataBufferUtils.join(imagePart.content())
                .flatMap(dataBuffer -> menuService.uploadImage(restaurantId, categoryIndex, menuIndex, imagePart.filename(), dataBuffer.asInputStream()))
                .map(CommonResponse::success)
                .defaultIfEmpty(CommonResponse.error("参数错误"));
    }
}
