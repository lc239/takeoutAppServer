package com.lc.takeoutApp.pojo.jsonEntity;

import com.google.gson.annotations.JsonAdapter;
import com.lc.takeoutApp.gsonAdapter.AddressAdapter;
import lombok.Data;

//表示用户的一个地址
@Data
public class Address {
    private String address;
    private String name;
    private String phone;
}
