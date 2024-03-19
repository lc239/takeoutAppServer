package com.lc.takeoutApp.converter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.lc.takeoutApp.pojo.jsonEntity.Address;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.ArrayList;

@ReadingConverter
public class AddressArrReadingConverter implements Converter<String, ArrayList<Address>> {
    @Override
    public ArrayList<Address> convert(String source) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(source, JsonArray.class);
        ArrayList<Address> addresses = new ArrayList<>();
        jsonArray.forEach(jsonElement -> addresses.add(gson.fromJson(jsonElement, Address.class)));
        return addresses;
    }
}
