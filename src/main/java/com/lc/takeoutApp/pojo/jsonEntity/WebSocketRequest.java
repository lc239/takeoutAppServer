package com.lc.takeoutApp.pojo.jsonEntity;

import lombok.Data;

@Data
public class WebSocketRequest {
    String token;
    int type; //0是用户下单时使用，1是店铺接单时使用
    Long targetId; //此消息要给谁发送
}