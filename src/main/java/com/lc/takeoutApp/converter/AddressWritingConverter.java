package com.lc.takeoutApp.converter;

import com.google.gson.Gson;
import com.lc.takeoutApp.pojo.jsonEntity.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@WritingConverter
@Component
public class AddressWritingConverter implements Converter<Address, String> {

    @Autowired
    Gson gson;

    @Override
    public String convert(Address source) {
        return gson.toJson(source);
    }
}
