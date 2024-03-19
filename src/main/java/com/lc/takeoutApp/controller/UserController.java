package com.lc.takeoutApp.controller;

import com.lc.takeoutApp.pojo.*;
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
    Mono<CommonResponse> getUserInfo(@RequestHeader("userId") Long userId){
        return userService.findById(userId)
                .map(CommonResponse::success)
                .defaultIfEmpty(CommonResponse.error("用户不存在"));
    }

//    注册成功返回两个token
    @PostMapping("/register")
    Mono<CommonResponse> register(@RequestBody User formUser){
        return userService.register(formUser).map(user -> {
            Tokens tokens = Tokens.genTokensById(user.getId());
            userService.setToken(tokens.getToken(), user.getId().toString(), Duration.ofDays(2))
                    .concatWith(userService.setToken(tokens.getRefreshToken(), user.getId().toString(), Duration.ofDays(4)))
                    .subscribe();
            return CommonResponse.success(tokens);
        }).defaultIfEmpty(CommonResponse.error("此手机号已注册"));
    }

    //登录成功返回两个token
    @PostMapping("/login")
    Mono<CommonResponse> login(@RequestBody User loginUser){
        return userService.findByPhone(loginUser.getPhone()).map(user -> {
            if(Md5Util.md5DigestAsHex(loginUser.getPassword()).equals(user.getPassword())){
                Tokens tokens = Tokens.genTokensById(user.getId());
                //保存token到redis
                userService.setToken(tokens.getToken(), user.getId().toString(), Duration.ofDays(2))
                        .concatWith(userService.setToken(tokens.getRefreshToken(), user.getId().toString(), Duration.ofDays(4)))
                        .reduce((aBoolean, aBoolean2) -> aBoolean && aBoolean2)
                        .subscribe();
                return CommonResponse.success(tokens);
            }
            else{
                return CommonResponse.error("密码错误");
            }
        }).defaultIfEmpty(CommonResponse.error(2, "该手机号未注册")).onErrorReturn(CommonResponse.error(3, "数据库错误"));
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
    Mono<CommonResponse> changeRole(
            @RequestHeader("userId") Long userId,
            @PathVariable("isSeller") Boolean isSeller,
            @PathVariable("set") Boolean set
    ){
        return userService.changeRole(userId, isSeller, set).map(CommonResponse::success).defaultIfEmpty(CommonResponse.error("用户不存在"));
    }

    @PutMapping("/upload/avatar")
    Mono<CommonResponse> uploadAvatar(
            @RequestHeader("userId") Long userId,
            @RequestPart("avatar") FilePart avatarPart
    ){
        return DataBufferUtils.join(avatarPart.content())
                .flatMap(dataBuffer -> userService.uploadAvatar(userId, avatarPart.filename(), dataBuffer.asInputStream()))
                .map(s -> CommonResponse.success(s))
                .defaultIfEmpty(CommonResponse.error("参数错误"));
    }

    //需要有订单才能评论，待完成
    @PutMapping("/comment/{orderId}")
    Mono<CommonResponse> addComment(@RequestHeader("userId") Long userId, @PathVariable("orderId") String orderId, @RequestBody RestaurantComment comment){
        return commentService.addComment(userId, orderId, comment)
                .map(comment1 -> CommonResponse.success(comment1))
                .defaultIfEmpty(CommonResponse.error("参数错误"));
    }

//    @GetMapping("/orders/{pageOffset}/{pageSize}")
//    Mono<CommonResponse> getOrders(
//            @RequestHeader("userId") Long userId,
//            @PathVariable("pageOffset") int pageOffset,
//            @PathVariable("pageSize") int pageSize
//    ){
//        return orderService.findOrders(userId, PageRequest.of(pageOffset, pageSize).withSort(Sort.Direction.DESC, "createTime"))
//                .collectList()
//                .map(orders -> CommonResponse.success(orders))
//                .defaultIfEmpty(CommonResponse.error("查询失败"));
//    }

    //骑手接单
//    @PatchMapping("/order/take/{orderId}")
//    Mono<CommonResponse> takeOrder(@RequestHeader("userId") Long userId, @PathVariable("orderId") String orderId){
//        return orderService.takeOrderByD(userId, orderId)
//                .map(order -> CommonResponse.success(order))
//                .defaultIfEmpty(CommonResponse.error("接单失败"));
//    }

//    //用户请求订单
//    @PutMapping("/order/put")
//    Mono<CommonResponse> putOrder(@RequestHeader("userId") Long userId, @RequestBody Order order){
//        order.setCreateTime(Instant.now());
//        return orderService.putOrder(userId, order)
//                .map(aBoolean -> {
//                    if(aBoolean){
//                        return CommonResponse.success();
//                    }
//                    else{
//                        return CommonResponse.error("下单失败，请刷新重试");
//                    }
//                });
//    }

    @DeleteMapping("/order/cancel/{orderId}")
    Mono<CommonResponse> cancelOrder(@RequestHeader("userId") Long userId, @PathVariable("orderId") String orderId){
        return null;
    }

    @PutMapping("/update/username")
    Mono<CommonResponse> updateUsername(@RequestHeader("userId") Long userId, @RequestBody User user){
        if(user.getUsername().length() > 10 || user.getUsername().isEmpty()){
            return Mono.just(CommonResponse.error("用户名格式不正确"));
        }
        return userService.updateUsernameById(user.getUsername(), userId)
                .map(user1 -> CommonResponse.success())
                .defaultIfEmpty(CommonResponse.error("更新失败，请检查参数"));
    }
}
