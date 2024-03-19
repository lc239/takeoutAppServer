package com.lc.takeoutApp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.lc.takeoutApp.pojo.CommonResponse;
import com.lc.takeoutApp.pojo.Order;
import com.lc.takeoutApp.pojo.Restaurant;
import com.lc.takeoutApp.pojo.jsonEntity.Address;
import com.lc.takeoutApp.pojo.jsonEntity.Category;
import com.lc.takeoutApp.pojo.jsonEntity.Menu;
import com.lc.takeoutApp.pojo.jsonEntity.OrderedMenu;
import com.lc.takeoutApp.pojo.UserRestaurant;
import com.lc.takeoutApp.repository.OrderRepository;
import com.lc.takeoutApp.repository.RestaurantCommentRepository;
import com.lc.takeoutApp.repository.RestaurantRepository;
import com.lc.takeoutApp.repository.UserRepository;
import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration;
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.lang.reflect.Array;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class DataTests {

    @Autowired
    R2dbcEntityTemplate r2dbcEntityTemplate;

    @Autowired
    DatabaseClient databaseClient;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    RestaurantCommentRepository commentRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    @Autowired
    R2dbcCustomConversions r2dbcCustomConversions;


    @Test
    void mySqlTest() throws IOException {
        //ConnectionFactory f = ConnectionFactories.get("r2dbc:mysql://localhost:3306/test");
        MySqlConnectionConfiguration conf = MySqlConnectionConfiguration.builder()
                .host("localhost").username("root").password("123456").database("takeoutapp").serverZoneId(ZoneId.of("Asia/Shanghai"))
                .build();
        ConnectionFactory f = MySqlConnectionFactory.from(conf);

        Mono.from(f.create()).flatMapMany(connection ->
                        connection.createStatement("SELECT username FROM user WHERE id >= ?min")
                                .bind("min", 1).execute())
                .flatMap(result -> result.map((row, rowMetadata) -> row.get("username", String.class)))
                .doOnNext(System.out::println)
                .subscribe();
        System.in.read();
    }

//    @Test
//    void r2dbcTest() throws IOException {
//        //Query By Criteria, QBC
//        Criteria criteria = Criteria.empty().and("id").is(1).and("username").is("test1");
//        Query query = Query.query(criteria);
//        r2dbcEntityTemplate.select(query, User.class) //User对应一个表的bean
//                .subscribe(System.out::println);
//        databaseClient.sql("select * from user where id=?")
//                .bind(0, 2)
//                .fetch()
//                .one()
//                .map(map -> {
//                    System.out.println("map = " + map);
//                    return new User(
//                            (Long) map.get("id"),
//                            map.get("username").toString(),
//                            map.get("password").toString(),
//                            map.get("phone").toString(),
//                            (Boolean) map.get("is_seller"),
//                            (Boolean) map.get("is_delivery_man"),
//                            map.get("image_url").toString(),
//                            (Instant) map.get("create_time"));
//                })
//                .subscribe(System.out::println);
//        userRepository.findAll().subscribe(System.out::println);
//        System.in.read();
//    }

    @Test
    void redisTest() throws IOException {
        reactiveStringRedisTemplate.opsForValue()
                .get("name")
                .log().subscribe();
        reactiveStringRedisTemplate.opsForValue().set("name", "a").subscribe();
        System.in.read();
    }


    @Test
    void restaurantQueryTest() throws IOException {
        restaurantRepository.findAll().log().subscribe(restaurant -> restaurant.getCategories().forEach(System.out::println));
        System.in.read();
    }

    @Test
    void commentPageTest() throws IOException {
        commentRepository.findAllByRestaurantId(5L, PageRequest.of(0, 2)).log().subscribe();
        System.in.read();
    }

    @Test
    void restaurantPageTest() throws IOException {
        restaurantRepository.findAllBy(PageRequest.of(1, 2)).log().subscribe();
        System.in.read();
    }

    @Test
    void findOrderTest() throws IOException {
        orderRepository.findAll().log().subscribe();
        reactiveStringRedisTemplate.opsForValue().get("1").log().subscribe();
        System.in.read();
    }

//    @Test
//    void takeOrderTest() throws IOException {
////        reactiveStringRedisTemplate.opsForValue().delete("name").log().subscribe();
//        Order order = new Order("1", 1L, 1L, null, new ArrayList<OrderedMenu>(), 1, 1, 1L, null, Instant.now(), null, false, true);
//        orderRepository.save(order).log().subscribe();
//        System.in.read();
//    }

//    @Test
//    void putOrderTest() throws IOException {
//        Order order = new Order("1", 1L, 1L, 1L, new ArrayList<OrderedMenu>(), 1, 1, 1L, null, Instant.now(), null, false, false);
//        Gson gson = new Gson();
//        System.out.println(gson.toJson(order));
//        reactiveStringRedisTemplate.opsForValue().set(order.getId(), gson.toJson(order), Duration.ofMinutes(5)).log().subscribe();
//        reactiveStringRedisTemplate.opsForValue().get("1").log().subscribe();
//        System.in.read();
//    }

    @Test
    void gsonTest(){
        String j = "[{\"id\":1}]";
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(j, JsonArray.class);
        List<UserRestaurant> userRestaurants = new ArrayList<>();
        jsonArray.forEach(jsonElement -> {
            System.out.println(jsonElement);
            UserRestaurant userRestaurant = gson.fromJson(jsonElement, UserRestaurant.class);
            System.out.println(userRestaurant.getId());
            userRestaurants.add(userRestaurant);
        });
        System.out.println(gson.toJson(userRestaurants));
    }

    @Test
    void getOrdersTest() throws IOException {
        orderRepository.findAllByUserId(2L, PageRequest.of(0, 2).withSort(Sort.Direction.DESC, "createTime")).collectList().log().subscribe();
        System.in.read();
    }
    @Test
    void concatTest() throws IOException {
        Mono.just(1).doOnNext(System.out::println).concatWith(Mono.just(2)).then().thenReturn(3).log().subscribe();
        System.in.read();
    }

    @Test
    void restaurantTest() throws IOException {
        Gson gson = new Gson();
        Address address = gson.fromJson("{'name':'a','phone':'a','address':'a'}", Address.class);
        System.out.println(address);
        ArrayList<Category> category = gson.fromJson("[{'name':'a', 'menus': [{'name':'a'}]}]", ArrayList.class);
        System.out.println(category);
        ArrayList<Address> addresses = new ArrayList<>();
        addresses.add(address);
        System.out.println(addresses);
        System.out.println(gson.toJson(addresses));
        userRepository.findById(2L).log().subscribe();
        System.in.read();
    }
}
