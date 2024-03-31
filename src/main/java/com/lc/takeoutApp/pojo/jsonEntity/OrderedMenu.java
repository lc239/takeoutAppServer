package com.lc.takeoutApp.pojo.jsonEntity;

import com.fasterxml.jackson.annotation.JsonView;
import com.lc.takeoutApp.view.View;
import lombok.AllArgsConstructor;
import lombok.Data;

//表示订单中的一个菜实体
@AllArgsConstructor
@Data
@JsonView(View.class)
public class OrderedMenu {
    private Integer categoryIndex;
    private String name;
    private Integer num;
    private Long price;
}
