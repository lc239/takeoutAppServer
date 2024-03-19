package com.lc.takeoutApp;

import com.lc.takeoutApp.pojo.Restaurant;
import com.lc.takeoutApp.pojo.User;
import com.lc.takeoutApp.pojo.UserRestaurant;
import com.lc.takeoutApp.repository.RestaurantRepository;
import com.lc.takeoutApp.repository.UserRepository;
import com.lc.takeoutApp.repository.UserRestaurantRepository;
import com.lc.takeoutApp.utils.Md5Util;
import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration;
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;

//@SpringBootTest
//class TakeoutAppApplicationTests {
//
//	@Autowired
//	R2dbcEntityTemplate r2dbcEntityTemplate;
//
//	@Autowired
//	DatabaseClient databaseClient;
//
//	@Autowired
//	UserRepository userRepository;
//
//	@Autowired
//	UserRestaurantRepository userRestaurantRepository;
//
//	@Autowired
//	RestaurantRepository restaurantRepository;
//
//	@Test
//	void loginTest() throws IOException {
//		Mono<User> userMono = userRepository.findByPhone("12345678910");
//		userMono.log().subscribe();
//		System.in.read();
//	}
//
////	@Test
////	void userRegisterTest() throws IOException {
////		User user = new User(null, "a", "111", "13344445555", null, null, null, null);
////		userRepository.findByPhone(user.getPhone()).hasElement().flatMap(aBoolean -> {
////			if(aBoolean) return null;
////			else{
////				user.setPassword(Md5Util.md5DigestAsHex(user.getPassword()));
////				Mono<User> result = userRepository.save(user);
////				result.subscribe();
////				return result;
////			}
////		}).log().subscribe();
////		System.in.read();
////	}
//
//	@Test
//	void restaurantRegisterTest() throws IOException {
//		Long userId = 1L;
//		String name = "测试店名";
//		userRestaurantRepository.findByUserId(userId).hasElement().flatMap(aBoolean -> {
//			if(aBoolean) return Mono.empty();
//			else {
//				System.out.println("分支2");
//				return restaurantRepository.save(new Restaurant(name))
//						.doOnNext(restaurant -> userRestaurantRepository.save(new UserRestaurant(null, userId, restaurant.getId())).subscribe());
//			}
//		}).log().subscribe();
//		System.in.read();
//	}
//}
