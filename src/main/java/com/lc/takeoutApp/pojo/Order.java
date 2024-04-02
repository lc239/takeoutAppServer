package com.lc.takeoutApp.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.lc.takeoutApp.pojo.jsonEntity.Address;
import com.lc.takeoutApp.pojo.jsonEntity.OrderedMenu;
import com.lc.takeoutApp.view.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("`order`")
public class Order {
    @JsonIgnore
    @Id
    private Long id;
    @JsonView(View.OrderDeliveryView.class)
    private String orderId;
    private Long userId;
    private Long restaurantId;
    private Long deliveryManId;
    private Long commentId;
    private ArrayList<OrderedMenu> menus;
    private Integer packPrice;
    private Integer deliveryPrice;
    private Long price;
    @JsonView(View.OrderDeliveryView.class)
    private Address address;
    @JsonView(View.OrderDeliveryView.class)
    private Instant createTime;
    private Instant completeTime;
    @Transient
    @JsonIgnore
    private Boolean taken; //表示是否被商家接单，被商家接单的会被加入redis的list中
}
