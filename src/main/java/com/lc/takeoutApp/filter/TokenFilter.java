package com.lc.takeoutApp.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.lc.takeoutApp.pojo.CommonResponse;
import com.lc.takeoutApp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.handler.DefaultWebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.util.List;

@Order(2)
@Component
public class TokenFilter implements WebFilter {

    @Autowired
    ReactiveStringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        PathPattern registerPattern=new PathPatternParser().parse("/user/register");
        PathPattern loginPattern=new PathPatternParser().parse("/user/login");
        ServerHttpRequest request=exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        PathContainer pathWithinApplication = request.getPath().pathWithinApplication();

        if (
                !registerPattern.matches(pathWithinApplication) &&
                !loginPattern.matches(pathWithinApplication)
        ){
            List<String> auths = request.getHeaders().get("Authorization");
            //认证失败使用此流
//            Mono<Void> unAuth = Mono.defer(() ->{
//                        response.setStatusCode(HttpStatusCode.valueOf(401));
//                System.out.println(exchange.getResponse().getStatusCode());
//                return ((DefaultWebFilterChain) chain).getHandler().handle(exchange.mutate().response(response).build());
//            });
            Mono<Void> unAuth = Mono.just(exchange).doOnNext(exchange1 -> response.setStatusCode(HttpStatusCode.valueOf(401))).then(Mono.empty());
//            Mono<Void> unAuth = Mono.just(exchange).doOnNext(exchange1 -> response.setStatusCode(HttpStatusCode.valueOf(401)));
            if(auths == null){
                return unAuth;
            }
            String token = auths.get(0).substring(7);
            return redisTemplate.opsForValue().get(token).map(Long::valueOf)
                    .map(id -> exchange.mutate().request(
                            request.mutate().header("userId", String.valueOf(id)).build()
                    ).build())
                    .flatMap(chain::filter)
                    .switchIfEmpty(unAuth); //没查到说明token过期，转到未认证流
//            try{
////                id = ((Integer) JwtUtil.parseToken(token).get("id")).longValue();
//            } catch (TokenExpiredException tee){
//                response.setStatusCode(HttpStatusCode.valueOf(401));
//                return Mono.empty();
//            }
//            ServerHttpRequest newRequest = request.mutate().header("userId", String.valueOf(id)).build();
//            exchange = exchange.mutate().request(newRequest).build();
        }

        return chain.filter(exchange);
    }
}
