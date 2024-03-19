package com.lc.takeoutApp.pojo;

import com.google.gson.JsonArray;
import com.google.gson.annotations.JsonAdapter;
import com.lc.takeoutApp.gsonAdapter.RestaurantAdapter;
import com.lc.takeoutApp.pojo.jsonEntity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("restaurant")
public class Restaurant {
    @Id
    private Long id;
    private String name;
    private String introduction;
    private Long saleNum;
    private Long rate;
    private Long rateCount;
    private String imageFilename;
    private Instant createTime;
    private ArrayList<Category> categories;
    private Long deliveryPrice;
}
