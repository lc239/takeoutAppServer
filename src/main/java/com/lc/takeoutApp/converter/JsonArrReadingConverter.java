package com.lc.takeoutApp.converter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.lc.takeoutApp.pojo.jsonEntity.OrderedMenu;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

@ReadingConverter
//通用的没能设置好，以后再说
public class JsonArrReadingConverter <T> implements Converter<String, ArrayList<T>> {
    @Override
    public ArrayList<T> convert(String source) {
        System.out.println(source);
        Gson gson = new Gson();
        ArrayList<T> result = new ArrayList<>();
        Type type = result.getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType)type;
        Type[] actualTypes = pt.getActualTypeArguments();
        JsonArray jsonArray = gson.fromJson(source, JsonArray.class);
        jsonArray.forEach(jsonElement -> result.add(gson.fromJson(jsonElement, actualTypes[0])));
        return result;
    }
}
