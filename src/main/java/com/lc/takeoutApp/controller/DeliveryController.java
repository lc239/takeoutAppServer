package com.lc.takeoutApp.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.lc.takeoutApp.pojo.CommonResponse;
import com.lc.takeoutApp.pojo.DeliveryMan;
import com.lc.takeoutApp.pojo.Order;
import com.lc.takeoutApp.service.DeliveryService;
import com.lc.takeoutApp.service.OrderService;
import com.lc.takeoutApp.view.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    OrderService orderService;

    @GetMapping("/info")
    Mono<CommonResponse<DeliveryMan>> getInfo(@RequestHeader("deliveryId") Long deliveryId){
        return deliveryService.findById(deliveryId)
                .map(CommonResponse::success)
                .defaultIfEmpty(CommonResponse.error("还不是骑手哦"));
    }

    @GetMapping("/order/delivering")
    Mono<CommonResponse<List<Order>>> getDeliveringOrders(@RequestHeader("deliveryId") Long deliveryId){
        return orderService.findDeliveringOrders(deliveryId).collectList().map(CommonResponse::success);
    }

//    @PostMapping("/register")
//    Mono<CommonResponse<DeliveryMan>> register(@RequestHeader("userId") Long userId){
//        return deliveryService.register(userId).map(CommonResponse::success);
//    }

    @JsonView(View.OrderDeliveryView.class)
    @GetMapping("/order/take/{indexStart}/{indexEnd}")
    Mono<CommonResponse<List<Order>>> getOrders(
            @PathVariable("indexStart") int indexStart,
            @PathVariable("indexEnd") int indexEnd
    ){
        return orderService.findWaitingDeliveryOrders(indexStart, indexEnd)
                .collectList()
                .map(CommonResponse::success);
    }

    @PatchMapping("/order/take/{orderId}")
    Mono<CommonResponse<Order>> takeOrder(@RequestHeader("deliveryId") Long deliveryId, @PathVariable("orderId") String orderId){
        return orderService.takeOrderByD(deliveryId, orderId)
                .map(CommonResponse::success)
                .defaultIfEmpty(CommonResponse.error("订单不存在或已被接单"));
    }

    @PatchMapping("/order/complete/{orderId}")
    Mono<CommonResponse<DeliveryMan>> completeOrder(@RequestHeader("deliveryId") Long deliveryId, @PathVariable("orderId") String orderId){
        return deliveryService.completeOrder(deliveryId, orderId)
                .map(CommonResponse::success)
                .defaultIfEmpty(CommonResponse.error("订单错误"));
    }
}
