package com.lc.takeoutApp.gsonAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.lc.takeoutApp.pojo.Restaurant;
import com.lc.takeoutApp.pojo.jsonEntity.Category;
import com.lc.takeoutApp.pojo.jsonEntity.Menu;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
//
//public class RestaurantAdapter extends TypeAdapter<Restaurant> {
//
//    @Override
//    public void write(JsonWriter jsonWriter, Restaurant restaurant) throws IOException {
//        jsonWriter.beginObject();
//        jsonWriter.name("id").value(restaurant.getId());
//        jsonWriter.name("name").value(restaurant.getName());
//        jsonWriter.name("introduction").value(restaurant.getIntroduction());
//        jsonWriter.name("saleNum").value(restaurant.getSaleNum());
//        jsonWriter.name("rate").value(restaurant.getRate());
//        jsonWriter.name("rateCount").value(restaurant.getRateCount());
//        jsonWriter.name("imageFilename").value(restaurant.getImageFilename());
//        jsonWriter.name("createTime").value(restaurant.getCreateTime().toString());
//        jsonWriter.name("deliveryPrice").value(restaurant.getDeliveryPrice());
//        jsonWriter.name("categories").beginArray();
//        for (Category category: restaurant.getCategories()) {
//            jsonWriter.beginObject();
//            jsonWriter.name("name").value(category.getName());
//            jsonWriter.name("menus").beginArray();
//            for (Menu menu: category.getMenus()) {
//                jsonWriter.beginObject();
//                jsonWriter.name("name").value(menu.getName());
//                jsonWriter.name("description").value(menu.getDescription());
//                jsonWriter.name("price").value(menu.getPrice());
//                jsonWriter.name("imageFilename").value(menu.getImageFilename());
//                jsonWriter.endObject();
//            }
//            jsonWriter.endObject();
//        }
//        jsonWriter.endArray();
//        jsonWriter.endObject();
//    }
//
//    @Override
//    public Restaurant read(JsonReader jsonReader) throws IOException {
//        Restaurant restaurant = new Restaurant();
//        jsonReader.beginObject();
//        while (jsonReader.hasNext()) {
//            switch (jsonReader.nextName()) {
//                case "id" -> restaurant.setId(jsonReader.nextLong());
//                case "name" -> restaurant.setName(jsonReader.nextString());
//                case "introduction" -> restaurant.setIntroduction(jsonReader.nextString());
//                case "saleNum" -> restaurant.setSaleNum(jsonReader.nextLong());
//                case "rate" -> restaurant.setRate(jsonReader.nextLong());
//                case "rateCount" -> restaurant.setRateCount(jsonReader.nextLong());
//                case "imageFilename" -> restaurant.setImageFilename(jsonReader.nextString());
//                case "createTime" -> restaurant.setCreateTime(Instant.parse(jsonReader.nextString()));
//                case "deliveryPrice" -> restaurant.setDeliveryPrice(jsonReader.nextLong());
//                case "categories" -> {
//                    ArrayList<Category> categories = new ArrayList<>();
//                    jsonReader.beginArray();
//                    while (jsonReader.hasNext()) {
//                        jsonReader.beginObject();
//                        Category category = new Category();
//                        switch (jsonReader.nextName()) {
//                            case "name" -> category.setName(jsonReader.nextString());
//                            case "menus" -> {
//                                ArrayList<Menu> menus = new ArrayList<>();
//                                jsonReader.beginArray();
//                                while (jsonReader.hasNext()) {
//                                    jsonReader.beginObject();
//                                    Menu menu = new Menu();
//                                    switch (jsonReader.nextName()) {
//                                        case "name" -> menu.setName(jsonReader.nextString());
//                                        case "description" -> menu.setDescription(jsonReader.nextString());
//                                        case "price" -> menu.setPrice(jsonReader.nextLong());
//                                        case "imageFilename" -> menu.setImageFilename(jsonReader.nextString());
//                                    }
//                                    menus.add(menu);
//                                    jsonReader.endObject();
//                                }
//                                jsonReader.endArray();
//                                category.setMenus(menus);
//                            }
//                        }
//                        categories.add(category);
//                        jsonReader.endObject();
//                    }
//                    jsonReader.endArray();
//                    restaurant.setCategories(categories);
//                }
//            }
//        }
//        jsonReader.endObject();
//        return restaurant;
//    }
//}
