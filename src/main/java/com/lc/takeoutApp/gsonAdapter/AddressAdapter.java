package com.lc.takeoutApp.gsonAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.lc.takeoutApp.pojo.jsonEntity.Address;

import java.io.IOException;
//
//public class AddressAdapter extends TypeAdapter<Address> {
//    @Override
//    public void write(JsonWriter jsonWriter, Address address) throws IOException {
//
//    }
//
//    @Override
//    public Address read(JsonReader jsonReader) throws IOException {
//        Address address = new Address();
//        jsonReader.beginObject();
//        while (jsonReader.hasNext()){
//            switch (jsonReader.nextName()){
//                case "name" -> address.setName(jsonReader.nextString());
//                case "address" -> address.setAddress(jsonReader.nextString());
//                case "phone" -> address.setPhone(jsonReader.nextString());
//            }
//        }
//        jsonReader.endObject();
//        return address;
//    }
//}
