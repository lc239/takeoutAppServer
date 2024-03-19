package com.lc.takeoutApp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.lc.takeoutApp.pojo.jsonEntity.Category;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;

public class CommonTests {

    private int count = 0;

    @Test
    void orderJsonTest(){
        Gson gson = new Gson();
        System.out.println(gson.toJson(Instant.now()));
    }

    @Test
    void retryTest(){
        Mono.just(getRandomS()).doOnNext(integer -> {
            if(integer == 1) throw new RuntimeException();
        }).retry(4).log().subscribe();
    }

    int getRandomS(){
        count = (count + 1) % 2;
        System.out.println(count);
        return count;
    }

    @Test
    void genericTest(){
        ArrayList<Integer> integers = new ArrayList<Integer>();
        Type type = integers.getClass().getGenericSuperclass();
        System.out.println(type);
        ParameterizedType pt = (ParameterizedType)type;
        Type[] actualTypes = pt.getActualTypeArguments();
        System.out.println(actualTypes[0]);
    }

    @Test
    void voidStreamTest() throws IOException {
        Mono.just(1).filter(integer -> integer > 1).hasElement().log().subscribe();
        System.in.read();
    }

    public ArrayList<Category> convert(String source) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(source, JsonArray.class);
        ArrayList<Category> categories = new ArrayList<>();
        jsonArray.forEach(jsonElement -> categories.add(gson.fromJson(jsonElement, Category.class)));
        return categories;
    }

    @Test
    void tt(){
        System.out.println(convert("[{\"name\": \"最新推荐\", \"menus\": [{\"name\": \"a\", \"price\": 10, \"description\": \"\", \"imageFilename\": \"default_menu_img.png\"}]}]"));
    }
}
