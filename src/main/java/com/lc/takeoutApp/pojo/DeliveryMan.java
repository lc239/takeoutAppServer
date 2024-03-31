package com.lc.takeoutApp.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@Table("delivery_man")
public class DeliveryMan {
    @Id
    @JsonIgnore
    private Long id;
    @JsonIgnore
    private Long userId;
    private ArrayList<String> deliveringOrders;
    private Long completeCount;
}
