package com.lc.takeoutApp.service;

import com.lc.takeoutApp.pojo.User;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.time.Duration;

public interface UserService {
    Mono<User> findByPhone(String phone);
    Mono<Boolean> setToken(String key, String value, Duration timeout);
    Mono<Boolean> deleteToken(String key);
    Mono<User> findById(Long id);
    Mono<User> register(User user); //注册成功返回user的save流，否则返回空的Mono
    Mono<User> changeRole(Long id, Boolean isSeller, Boolean set);
    Mono<String> uploadAvatar(Long id, String filename, InputStream inputStream);
    Mono<User> updateUsernameById(String username, Long id);
}
