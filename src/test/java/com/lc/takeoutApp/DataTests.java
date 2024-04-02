package com.lc.takeoutApp;

import com.lc.takeoutApp.pojo.Order;
import com.lc.takeoutApp.pojo.jsonEntity.Address;
import com.lc.takeoutApp.pojo.jsonEntity.OrderedMenu;
import com.lc.takeoutApp.repository.OrderRepository;
import com.lc.takeoutApp.repository.RestaurantCommentRepository;
import com.lc.takeoutApp.repository.RestaurantRepository;
import com.lc.takeoutApp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.r2dbc.core.DatabaseClient;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

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
    public void f() throws IOException {
        Order order = new Order("1", 1L, 20L, 1L, null, new ArrayList<>(), 0, 0, 1L, new Address("", "", ""), Instant.now(), Instant.now(), false);
        orderRepository.save(order).log().subscribe();
        System.in.read();
    }
}
