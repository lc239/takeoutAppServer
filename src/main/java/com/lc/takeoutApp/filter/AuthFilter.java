package com.lc.takeoutApp.filter;

import com.lc.takeoutApp.pojo.UserRestaurant;
import com.lc.takeoutApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.handler.DefaultWebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

@Order(3)
@Component
public class AuthFilter implements WebFilter {

    @Autowired
    UserRepository userRepository;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        PathPattern restaurantPattern=new PathPatternParser().parse("/restaurant");
        PathPattern deliveryPattern=new PathPatternParser().parse("/delivery");
        ServerHttpRequest request=exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        Mono<Void> unAuth = Mono.just(exchange).doOnNext(exchange1 -> response.setStatusCode(HttpStatusCode.valueOf(401))).then();

        PathContainer pathWithinApplication = request.getPath().pathWithinApplication();

        if(restaurantPattern.matches(pathWithinApplication) || deliveryPattern.matches(pathWithinApplication)){
            return userRepository.findById(Long.valueOf(request.getHeaders().get("userId").get(0)))
                    .map(user -> {
                        if(user.getIsDeliveryMan() && deliveryPattern.matches(pathWithinApplication)){
                            return true;
                        }
                        if(user.getIsSeller() && restaurantPattern.matches(pathWithinApplication)){
                            return true;
                        }
                        return false;
                    })
                    .flatMap(aBoolean -> {
                        if(aBoolean) return chain.filter(exchange);
                        else return unAuth;
                    });
        }
        return chain.filter(exchange);
    }
}
