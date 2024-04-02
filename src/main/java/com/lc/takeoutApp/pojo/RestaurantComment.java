package com.lc.takeoutApp.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.List;

@Table("restaurant_comment")
@Data
public class RestaurantComment {
    @Id
    private Long id;
    private Long userId;
    private String username;
    private Long restaurantId;
    private String content;
    private Integer rate;
    private List<String> images;
    private Instant createTime;
}
