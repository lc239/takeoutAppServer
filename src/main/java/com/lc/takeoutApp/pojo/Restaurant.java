package com.lc.takeoutApp.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.lc.takeoutApp.pojo.jsonEntity.Category;
import com.lc.takeoutApp.view.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("restaurant")
public class Restaurant {
    @Id
    @JsonView({View.RestaurantPreviewView.class, View.RestaurantSearchView.class})
    private Long id;
    @JsonView({View.RestaurantPreviewView.class, View.RestaurantSearchView.class})
    private String name;
    private String introduction;
    @JsonView(View.RestaurantPreviewView.class)
    private Long saleNum;
    @JsonView({View.RestaurantPreviewView.class, View.RestaurantSearchView.class})
    private Long rate;
    @JsonView({View.RestaurantPreviewView.class, View.RestaurantSearchView.class})
    private Long rateCount;
    @JsonView(View.RestaurantPreviewView.class)
    private String imageFilename;
    @JsonIgnore
    private Instant createTime;
    private ArrayList<Category> categories;
    @JsonView(View.RestaurantPreviewView.class)
    private Integer deliveryPrice;
}
