package com.lc.takeoutApp.pojo.jsonEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@AllArgsConstructor
@Data
public class WebSocketResponse<T> {
    int type; //0表示有用户下单，1表示商家接单，2表示商家拒绝，3表示骑手接单，4表示骑手完成
    Instant time;
    T data;

    public static WebSocketResponse<String> takeOrderByRResponse(String orderId){
        return new WebSocketResponse<>(1, Instant.now(), orderId);
    }

    public static WebSocketResponse<String> completeOrderResponse(String orderId){
        return new WebSocketResponse<>(4, Instant.now(), orderId);
    }

    public static WebSocketResponse<String> rejectOrderResponse(){
        return new WebSocketResponse<>(2, Instant.now(), null);
    }
}
