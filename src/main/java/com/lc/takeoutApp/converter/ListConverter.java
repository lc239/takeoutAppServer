package com.lc.takeoutApp.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.List;

@WritingConverter
public class ListConverter implements Converter<List<String>,String> {

    //用来把List<String>写入数据库
    @Override
    public String convert(List<String> source) {
        if(source.size() == 0) return "";
        return source.stream().reduce((s, s2) -> s + "," + s2).get();
    }
}
