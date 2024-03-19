package com.lc.takeoutApp.pojo.jsonEntity;

import lombok.Data;

//表示订单中的一个菜实体
@Data
public class OrderedMenu {
    private Long id;
    private String name;
    private Integer num;
    private Long price;
}
