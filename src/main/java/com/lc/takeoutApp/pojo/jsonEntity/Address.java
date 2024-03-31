package com.lc.takeoutApp.pojo.jsonEntity;

import com.fasterxml.jackson.annotation.JsonView;
import com.lc.takeoutApp.view.View;
import lombok.AllArgsConstructor;
import lombok.Data;

//表示用户的一个地址
@AllArgsConstructor
@Data
@JsonView(View.class)
public class Address {
    private String address;
    private String name;
    private String phone;
}
