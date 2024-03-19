package com.lc.takeoutApp.pojo;

import com.google.gson.annotations.JsonAdapter;
import com.lc.takeoutApp.gsonAdapter.OrderAdapter;
import com.lc.takeoutApp.pojo.jsonEntity.OrderedMenu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("`order`")
public class Order {
    private String id;
    private Long userId;
    private Long restaurantId;
    private Long deliveryManId;
    private ArrayList<OrderedMenu> menus;
    private Integer packPrice;
    private Integer deliveryPrice;
    private Long price;
    private String address;
    private Instant createTime;
    private Instant completeTime;
}
