package com.lc.takeoutApp.service;

import com.lc.takeoutApp.pojo.User;
import com.lc.takeoutApp.pojo.jsonEntity.Address;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public interface UserService {
    Mono<User> findByPhone(String phone);
    Mono<Boolean> setToken(String key, String value, Duration timeout);
    Mono<Boolean> deleteToken(String key);
    Mono<User> findById(Long id);
    Mono<User> register(User user); //注册成功返回user的save流，否则返回空的Mono
    Mono<User> changeRole(Long id, Boolean isSeller, Boolean set);
    Mono<String> uploadAvatar(Long id, String filename, InputStream inputStream);
    Mono<User> updateUsernameById(String username, Long id);
    Mono<User> addAddress(Long id, Address address);
    Mono<User> setAddress(Long id, ArrayList<Address> addresses);
}
