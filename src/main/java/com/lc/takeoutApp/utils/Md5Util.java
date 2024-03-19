package com.lc.takeoutApp.utils;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class Md5Util {

    public static String md5DigestAsHex(String origin){
        return DigestUtils.md5DigestAsHex(origin.getBytes(StandardCharsets.UTF_8));
    }
}
