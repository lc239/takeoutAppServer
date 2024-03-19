package com.lc.takeoutApp.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@Table("user_restaurant")
public class UserRestaurant {
    @Id
    private Long id;
    private Long userId;
    private Long restaurantId;
}
