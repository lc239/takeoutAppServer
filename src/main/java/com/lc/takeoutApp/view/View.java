package com.lc.takeoutApp.view;

//View被Result类的所有属性添加@JsonView，用来控制响应中应该被序列化的字段
public interface View {
    interface RestaurantView extends View{}
    interface RestaurantPreviewView extends RestaurantView{}
    interface RestaurantSearchView extends RestaurantView{}

    interface OrderView extends View{}
    interface OrderDeliveryView extends OrderView{}
}
