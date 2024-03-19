package com.lc.takeoutApp.pojo.jsonEntity;

import lombok.Data;

@Data
public class Menu {
    private String name;
    private String description;
    private Long price;
    private String imageFilename;
}
