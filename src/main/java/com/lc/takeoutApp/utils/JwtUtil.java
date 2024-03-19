package com.lc.takeoutApp.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private static final String SECRET = "fakeSecret";

    public static String genToken(Map<String, Object> claims, int duration){
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + duration))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public static Map<String, Object> parseToken(String token){
        return JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
    }
}
