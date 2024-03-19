package com.lc.takeoutApp.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.JsonAdapter;
import com.lc.takeoutApp.gsonAdapter.UserAdapter;
import com.lc.takeoutApp.pojo.jsonEntity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.ArrayList;

@Table("user")
@Data
public class User {

    @Id
    private Long id;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String phone;
    private ArrayList<Address> addresses;
    private Boolean isSeller;
    private Boolean isDeliveryMan;
    private String avatarFilename;
    @JsonIgnore
    private Instant createTime;
}
