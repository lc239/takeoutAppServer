package com.lc.takeoutApp.pojo.jsonEntity;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Category {
    private String name;
    private ArrayList<Menu> menus;
}
