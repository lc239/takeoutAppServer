package com.lc.takeoutApp.converter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.lc.takeoutApp.pojo.jsonEntity.Address;
import com.lc.takeoutApp.pojo.jsonEntity.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.ArrayList;

@ReadingConverter
public class CategoryArrReadingConverter implements Converter<String, ArrayList<Category>> {
    @Override
    public ArrayList<Category> convert(String source) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(source, JsonArray.class);
        ArrayList<Category> categories = new ArrayList<>();
        jsonArray.forEach(jsonElement -> categories.add(gson.fromJson(jsonElement, Category.class)));
        return categories;
    }
}
