package com.lc.takeoutApp.converter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.lc.takeoutApp.pojo.jsonEntity.Category;
import com.lc.takeoutApp.pojo.jsonEntity.OrderedMenu;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.ArrayList;

@ReadingConverter
public class OrderedMenuArrReadingConverter implements Converter<String, ArrayList<OrderedMenu>> {
    @Override
    public ArrayList<OrderedMenu> convert(String source) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(source, JsonArray.class);
        ArrayList<OrderedMenu> menus = new ArrayList<>();
        jsonArray.forEach(jsonElement -> menus.add(gson.fromJson(jsonElement, OrderedMenu.class)));
        return menus;
    }
}
