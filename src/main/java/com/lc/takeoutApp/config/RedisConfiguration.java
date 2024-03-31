package com.lc.takeoutApp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lc.takeoutApp.pojo.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//@Configuration
public class RedisConfiguration {

//    @Bean
//    ReactiveRedisOperations<String, Order> redisOperations(ReactiveRedisConnectionFactory factory){
//        ObjectMapper om = JsonMapper.builder().addModule(new JavaTimeModule()).build();
//        Jackson2JsonRedisSerializer<Order> serializer = new Jackson2JsonRedisSerializer<>(om, Order.class);
//        RedisSerializationContext.RedisSerializationContextBuilder<String, Order> builder =
//                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
//        RedisSerializationContext<String, Order> context = builder.value(serializer).build();
//        return new ReactiveRedisTemplate<>(factory, context);
//    }
}
