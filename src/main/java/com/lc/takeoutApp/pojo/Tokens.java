package com.lc.takeoutApp.pojo;

import com.lc.takeoutApp.utils.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class Tokens{
    private String token;
    private String refreshToken;

    public static Tokens genTokensById(Long id){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        String token = JwtUtil.genToken(claims, 1000 * 60 * 60 * 24); //1天
        String refreshToken = JwtUtil.genToken(claims, 1000 * 60 * 60 * 24 * 2); //2倍时间的刷新token
        return new Tokens(token, refreshToken);
    }
}