package com.lc.takeoutApp.converter;

import com.google.gson.Gson;
import com.lc.takeoutApp.pojo.jsonEntity.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@ReadingConverter
@Component
public class AddressReadingConverter implements Converter<String, Address> {

    @Autowired
    Gson gson;

    @Override
    public Address convert(String source) {
        return gson.fromJson(source, Address.class);
    }
}
