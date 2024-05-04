package com.lc.takeoutApp.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@Table("delivery_man")
public class DeliveryMan {
    @Id
    @JsonIgnore
    private Long id;
    private Long completeCount;

    public Long completeOne(){
        completeCount++;
        return completeCount;
    }
}
