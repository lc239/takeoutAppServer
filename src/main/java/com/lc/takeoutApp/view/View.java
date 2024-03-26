package com.lc.takeoutApp.view;

//View被Result类的所有属性添加@JsonView
public interface View {
    public interface RestaurantView extends View{}
    public interface RestaurantPreviewView extends RestaurantView{}
    public interface RestaurantSearchView extends RestaurantView{}
}
