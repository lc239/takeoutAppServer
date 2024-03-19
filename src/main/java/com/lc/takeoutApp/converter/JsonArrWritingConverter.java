package com.lc.takeoutApp.converter;

import com.google.gson.Gson;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.ArrayList;

@WritingConverter
public class JsonArrWritingConverter <T> implements Converter<ArrayList<T>, String> {
    @Override
    public String convert(ArrayList<T> source) {
        Gson gson = new Gson();
        return gson.toJson(source);
    }
}
