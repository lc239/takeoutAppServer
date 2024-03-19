package com.lc.takeoutApp.gsonAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.lc.takeoutApp.pojo.Order;
import com.lc.takeoutApp.pojo.User;
import com.lc.takeoutApp.pojo.jsonEntity.Address;
import com.lc.takeoutApp.pojo.jsonEntity.OrderedMenu;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

public class UserAdapter extends TypeAdapter<User> {
    @Override
    public void write(JsonWriter jsonWriter, User user) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("id").value(user.getId());
        jsonWriter.name("username").value(user.getUsername());
        jsonWriter.name("password").value(user.getPassword());
        jsonWriter.name("phone").value(user.getPhone());
        jsonWriter.name("addresses").beginArray();
        for (Address address: user.getAddresses()) {
            jsonWriter.beginObject();
            jsonWriter.name("address").value(address.getAddress());
            jsonWriter.name("name").value(address.getName());
            jsonWriter.name("phone").value(address.getPhone());
            jsonWriter.endObject();
        }
        jsonWriter.endArray();
        jsonWriter.name("isSeller").value(user.getIsSeller());
        jsonWriter.name("isDeliveryMan").value(user.getIsDeliveryMan());
        jsonWriter.name("avatarFilename").value(user.getAvatarFilename());
        jsonWriter.name("createTime").value(user.getCreateTime().toString());
        jsonWriter.endObject();
    }

    @Override
    public User read(JsonReader jsonReader) throws IOException {
        User user = new User();
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            switch (jsonReader.nextName()) {
                case "id" -> user.setId(jsonReader.nextLong());
                case "username" -> user.setUsername(jsonReader.nextString());
                case "password" -> user.setPassword(jsonReader.nextString());
                case "phone" -> user.setPhone(jsonReader.nextString());
                case "addresses" -> {
                    ArrayList<Address> addresses = new ArrayList<>();
                    jsonReader.beginArray();
                    while(jsonReader.hasNext()){
                        jsonReader.beginObject();
                        Address address = new Address();
                        while (jsonReader.hasNext()){
                            switch (jsonReader.nextName()){
                                case "address" -> address.setAddress(jsonReader.nextString());
                                case "phone" -> address.setPhone(jsonReader.nextString());
                                case "name" -> address.setName(jsonReader.nextString());
                            }
                        }
                        addresses.add(address);
                        jsonReader.endObject();
                    }
                    jsonReader.endArray();
                    user.setAddresses(addresses);
                }
                case "isSeller" -> user.setIsSeller(jsonReader.nextBoolean());
                case "isDeliveryMan" -> user.setIsDeliveryMan(jsonReader.nextBoolean());
                case "avatarFilename" -> user.setAvatarFilename(jsonReader.nextString());
                case "createTime" -> user.setCreateTime(Instant.parse(jsonReader.nextString()));
            }
        }
        jsonReader.endObject();
        return user;
    }
}
