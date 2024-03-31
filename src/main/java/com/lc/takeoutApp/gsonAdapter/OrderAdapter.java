package com.lc.takeoutApp.gsonAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.lc.takeoutApp.pojo.Order;
import com.lc.takeoutApp.pojo.jsonEntity.OrderedMenu;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

//public class OrderAdapter extends TypeAdapter<Order> {
//    @Override
//    public void write(JsonWriter jsonWriter, Order order) throws IOException {
//        jsonWriter.beginObject();
//        jsonWriter.name("id").value(order.getId());
//        jsonWriter.name("userId").value(order.getUserId());
//        jsonWriter.name("restaurantId").value(order.getRestaurantId());
//        jsonWriter.name("deliveryManId").value(order.getDeliveryManId());
//        jsonWriter.name("menus").beginArray();
//        for (OrderedMenu menu: order.getMenus()) {
//            jsonWriter.beginObject();
//            jsonWriter.name("name").value(menu.getName());
//            jsonWriter.name("num").value(menu.getNum());
//            jsonWriter.name("price").value(menu.getPrice());
//            jsonWriter.endObject();
//        }
//        jsonWriter.endArray();
//        jsonWriter.name("packPrice").value(order.getPackPrice());
//        jsonWriter.name("deliveryPrice").value(order.getDeliveryPrice());
//        jsonWriter.name("price").value(order.getPrice());
//        jsonWriter.name("address").value(order.getAddress());
//        jsonWriter.name("createTime").value(order.getCreateTime().toString());
//        jsonWriter.endObject();
//    }
//
//    @Override
//    public Order read(JsonReader jsonReader) throws IOException {
//        Order order = new Order();
//        jsonReader.beginObject();
//        while (jsonReader.hasNext()) {
//            switch (jsonReader.nextName()) {
//                case "id" -> order.setId(jsonReader.nextString());
//                case "userId" -> order.setUserId(jsonReader.nextLong());
//                case "restaurantId" -> order.setRestaurantId(jsonReader.nextLong());
//                case "deliveryManId" -> order.setDeliveryManId(jsonReader.nextLong());
//                case "menus" -> {
//                    ArrayList<OrderedMenu> menus = new ArrayList<>();
//                    jsonReader.beginArray();
//                    while(jsonReader.hasNext()){
//                        jsonReader.beginObject();
//                        OrderedMenu menu = new OrderedMenu();
//                        while (jsonReader.hasNext()){
//                            switch (jsonReader.nextName()){
//                                case "num" -> menu.setNum(jsonReader.nextInt());
//                                case "name" -> menu.setName(jsonReader.nextString());
//                                case "price" -> menu.setPrice(jsonReader.nextLong());
//                            }
//                        }
//                        menus.add(menu);
//                        jsonReader.endObject();
//                    }
//                    jsonReader.endArray();
//                    order.setMenus(menus);
//                }
//                case "packPrice" -> order.setPackPrice(jsonReader.nextInt());
//                case "deliveryPrice" -> order.setDeliveryPrice(jsonReader.nextInt());
//                case "price" -> order.setPrice(jsonReader.nextLong());
//                case "address" -> order.setAddress(jsonReader.nextString());
//                case "createTime" -> order.setCreateTime(Instant.parse(jsonReader.nextString()));
//                case "completeTime" -> order.setCompleteTime(Instant.parse(jsonReader.nextString()));
//            }
//        }
//        jsonReader.endObject();
//        return order;
//    }
//}
