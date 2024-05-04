package com.lc.takeoutApp.controller;

import com.lc.takeoutApp.pojo.*;
import com.lc.takeoutApp.pojo.jsonEntity.Address;
import com.lc.takeoutApp.service.OrderService;
import com.lc.takeoutApp.service.RestaurantCommentService;
import com.lc.takeoutApp.service.UserService;
import com.lc.takeoutApp.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.codec.multipart.*;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Autowired
    RestaurantCommentService commentService;

    @GetMapping("/info")
    Mono<CommonResponse<User>> getUserInfo(@RequestHeader("userId") Long userId){
        return userService.findById(userId)
                .map(CommonResponse::success)
                .defaultIfEmpty(CommonResponse.error("用户不存在"));
    }

//    注册成功返回两个token
    @PostMapping("/register")
    Mono<CommonResponse<Tokens>> register(@RequestBody User formUser){
        return userService.register(formUser).map(user -> {
            Tokens tokens = Tokens.genTokensById(user.getId());
            userService.setToken(tokens.getToken(), user.getId().toString(), Duration.ofDays(2))
                    .concatWith(userService.setToken(tokens.getRefreshToken(), user.getId().toString(), Duration.ofDays(4)))
                    .subscribe();
            return CommonResponse.success(tokens);
        }).onErrorResume(throwable -> Mono.just(CommonResponse.error(throwable.getMessage())));
    }

    //登录成功返回两个token
    @PostMapping("/login")
    Mono<CommonResponse<Tokens>> login(@RequestBody User loginUser){
        return userService.findByPhone(loginUser.getPhone()).flatMap(user -> {
            if(Md5Util.md5DigestAsHex(loginUser.getPassword()).equals(user.getPassword())){
                Tokens tokens = Tokens.genTokensById(user.getId());
                //保存token到redis
                userService.setToken(tokens.getToken(), user.getId().toString(), Duration.ofDays(2))
                        .concatWith(userService.setToken(tokens.getRefreshToken(), user.getId().toString(), Duration.ofDays(4)))
                        .reduce((aBoolean, aBoolean2) -> aBoolean && aBoolean2)
                        .subscribe();
                return Mono.just(CommonResponse.success(tokens));
            }
            else{
                return Mono.error(new Throwable("密码错误"));
            }
        }).onErrorResume(throwable -> Mono.just(CommonResponse.error(throwable.getMessage())))
                .defaultIfEmpty(CommonResponse.error(2, "该手机号未注册"));
    }

    //刷新token
    @GetMapping("/refresh")
    Mono<CommonResponse> refreshToken(@RequestHeader("userId") Long userId, @RequestHeader("Authorization") String refreshToken){
        Tokens tokens = Tokens.genTokensById(userId);

        return userService.deleteToken(refreshToken.substring(7))
                .concatWith(userService.setToken(tokens.getToken(), userId.toString(), Duration.ofDays(2)))
                .concatWith(userService.setToken(tokens.getRefreshToken(), userId.toString(), Duration.ofDays(4)))
                .then().thenReturn(CommonResponse.success(tokens));
    }

    //isSeller是0,1分别表示骑手和商家，set是0,1表示删除或注册
    @PutMapping("/role/{isSeller}/{set}")
    Mono<CommonResponse<User>> changeRole(
            @RequestHeader("userId") Long userId,
            @PathVariable("isSeller") Boolean isSeller,
            @PathVariable("set") Boolean set
    ){
        return userService.changeRole(userId, isSeller, set).map(CommonResponse::success).defaultIfEmpty(CommonResponse.error("用户不存在"));
    }

    @PutMapping("/upload/avatar")
    Mono<CommonResponse<String>> uploadAvatar(
            @RequestHeader("userId") Long userId,
            @RequestPart("avatar") FilePart avatarPart
    ){
        return DataBufferUtils.join(avatarPart.content())
                .flatMap(dataBuffer -> userService.uploadAvatar(userId, avatarPart.filename(), dataBuffer.asInputStream()))
                .map(CommonResponse::success)
                .defaultIfEmpty(CommonResponse.error("参数错误"));
    }

    @GetMapping("/orders/{pageOffset}/{pageSize}")
    Mono<CommonResponse<List<Order>>> getOrders(
            @RequestHeader("userId") Long userId,
            @PathVariable("pageOffset") int pageOffset,
            @PathVariable("pageSize") int pageSize
    ){
        return orderService.findHistoryOrders(userId, PageRequest.of(pageOffset, pageSize).withSort(Sort.Direction.DESC, "createTime"))
                .collectList()
                .map(CommonResponse::success);
    }

    @GetMapping("/orders/delivering")
    Mono<CommonResponse<List<Order>>> getDeliveringOrders(@RequestHeader("userId") Long userId){
        return orderService.findUsersDeliveringOrders(userId).collectList().map(CommonResponse::success);
    }

    //用户请求订单
    @PutMapping("/order/put/{restaurantId}")
    Mono<CommonResponse<Order>> putOrder(@RequestHeader("userId") Long userId,@PathVariable("restaurantId") Long restaurantId, @RequestBody Order order){
        return orderService.putOrder(userId, restaurantId, order)
                .map(CommonResponse::success)
                .defaultIfEmpty(CommonResponse.error("下单失败，商家可能已下线"));
    }

//    @DeleteMapping("/order/cancel/{orderId}")
//    Mono<CommonResponse> cancelOrder(@RequestHeader("userId") Long userId, @PathVariable("orderId") String orderId){
//        return null;
//    }

    @PutMapping("/order/comment/{orderId}")
    Mono<CommonResponse<RestaurantComment>> commentOrder(
            @RequestHeader("userId") Long userId,
            @PathVariable("orderId") String orderId,
            @RequestBody RestaurantComment comment
    ){
        comment.setUserId(userId);
        return commentService.addComment(orderId, comment)
                .map(CommonResponse::success)
                .defaultIfEmpty(CommonResponse.error("评论失败"));
    }

    @PutMapping("/update/username")
    Mono<CommonResponse<Void>> updateUsername(@RequestHeader("userId") Long userId, @RequestBody User user){
        if(user.getUsername().length() > 10 || user.getUsername().isEmpty()){
            return Mono.just(CommonResponse.error("用户名格式不正确"));
        }
        return userService.updateUsernameById(user.getUsername(), userId)
                .map(user1 -> CommonResponse.success())
                .defaultIfEmpty(CommonResponse.error("更新失败，请检查参数"));
    }

    @PatchMapping("/address/add")
    Mono<CommonResponse<Void>> addAddress(@RequestHeader("userId") Long userId, @RequestBody Address address){
        return userService.addAddress(userId, address)
                .map(user -> CommonResponse.success())
                .defaultIfEmpty(CommonResponse.error("请检查添加地址规则"));
    }

    @PutMapping("/address/set")
    Mono<CommonResponse<Void>> setAddress(@RequestHeader("userId") Long userId, @RequestBody ArrayList<Address> addresses){
        return userService.setAddress(userId, addresses).hasElement().map(aBoolean -> {
            if(aBoolean) return CommonResponse.success();
            else return CommonResponse.error("地址规则有误");
        });
    }
}
