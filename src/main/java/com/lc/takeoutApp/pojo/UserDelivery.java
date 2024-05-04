package com.lc.takeoutApp.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@Table("user_delivery")
public class UserDelivery {
    @Id
    Long id;
    Long userId;
    Long deliveryManId;
}
