package com.lc.takeoutApp.service.impl;

import com.lc.takeoutApp.pojo.User;
import com.lc.takeoutApp.pojo.jsonEntity.Address;
import com.lc.takeoutApp.repository.UserRepository;
import com.lc.takeoutApp.service.UserService;
import com.lc.takeoutApp.utils.AliOssUtil;
import com.lc.takeoutApp.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final String DEFAULT_AVATAR_FILENAME = "default_user_avatar.png";

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    @Autowired
    AliOssUtil aliOssUtil;

    @Override
    public Mono<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public Mono<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Mono<User> register(User user) {
        return userRepository.findByPhone(user.getPhone()).hasElement().flatMap(aBoolean -> {
            if(aBoolean){
                return Mono.empty();
            }
            else{
                user.setPassword(Md5Util.md5DigestAsHex(user.getPassword()));// 密码加密
                //初始化
                user.setAddresses(new ArrayList<>());
                user.setAvatarFilename(DEFAULT_AVATAR_FILENAME);
                user.setIsDeliveryMan(false);
                user.setIsSeller(false);
                return userRepository.save(user);
            }
        });
    }

    @Override
    public Mono<User> changeRole(Long id, Boolean isSeller, Boolean set) {
        return userRepository.findById(id).flatMap(user -> {
            if(isSeller && (user.getIsSeller() ^ set)){
                user.setIsSeller(set);
            } else if(!isSeller && (user.getIsDeliveryMan() ^ set)){
                user.setIsDeliveryMan(set);
            } else {
                return Mono.just(user);
            }
            return userRepository.save(user);
        });
    }

    @Override
    public Mono<String> uploadAvatar(Long id, String filename, InputStream inputStream) {
        String filePostfix = filename.substring(filename.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String tmpFilename = uuid + filePostfix;
        while (true){
            //保证文件名不重复
            if(aliOssUtil.objectExist(tmpFilename)){
                tmpFilename = UUID.randomUUID().toString().replaceAll("-", "") + filePostfix;
            }
            else break;
        }
        String newFilename = tmpFilename;
        return userRepository.findById(id).flatMap(user -> {
            boolean uploadResult = aliOssUtil.upload(newFilename, inputStream);
            if(uploadResult){
                //如果之前不是默认头像，删除之前的文件
                if(!user.getAvatarFilename().equals(DEFAULT_AVATAR_FILENAME)){
                    aliOssUtil.delete(user.getAvatarFilename());
                }
                user.setAvatarFilename(newFilename);
                return userRepository.save(user).thenReturn(newFilename);
            }
            else return Mono.empty();
        });
    }

    @Override
    public Mono<User> updateUsernameById(String username, Long id) {
        return findById(id).doOnNext(user -> user.setUsername(username)).flatMap(user -> userRepository.save(user));
    }

    @Override
    public Mono<User> addAddress(Long id, Address address) {
        return userRepository.findById(id)
                .filter(user -> user.getAddresses().size() <= 6)
                .filter(user -> address.getAddress().length() <= 100 && address.getName().length() <= 4)
                .doOnNext(user -> user.getAddresses().add(address))
                .flatMap(user -> userRepository.save(user));
    }

    @Override
    public Mono<Boolean> setToken(String key, String value, Duration timeout) {
        return reactiveStringRedisTemplate.opsForValue().set(key, value, timeout);
    }

    @Override
    public Mono<Boolean> deleteToken(String key) {
        return reactiveStringRedisTemplate.opsForValue().delete(key);
    }
}
