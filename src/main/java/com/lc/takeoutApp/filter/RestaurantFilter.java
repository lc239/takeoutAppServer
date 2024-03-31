package com.lc.takeoutApp.filter;

import com.lc.takeoutApp.pojo.UserRestaurant;
import com.lc.takeoutApp.repository.UserRestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.handler.DefaultWebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

@Order(4)
@Component
public class RestaurantFilter implements WebFilter {

    @Autowired
    UserRestaurantRepository userRestaurantRepository;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        //有些路径不需要这个过滤器，但过滤不会有明显副作用
        PathPattern menuPattern=new PathPatternParser().parse("/menu/**");
        PathPattern restaurantPattern = new PathPatternParser().parse("/restaurant/**");
        ServerHttpRequest request=exchange.getRequest();

        PathContainer pathWithinApplication = request.getPath().pathWithinApplication();

        if (
                menuPattern.matches(pathWithinApplication) ||
                restaurantPattern.matches(pathWithinApplication)
        ){
            //没有说明上一层token过滤器失效调用此处
//            if(request.getHeaders().get("userId") == null){
//                return Mono.defer(() -> ((DefaultWebFilterChain) chain).getHandler().handle(exchange));
//            }
            return userRestaurantRepository.findByUserId(Long.valueOf(request.getHeaders().get("userId").get(0)))
                    .map(UserRestaurant::getRestaurantId)
                    .map(restaurantId -> request.mutate().header("restaurantId", String.valueOf(restaurantId)).build())
                    .flatMap(request1 -> chain.filter(exchange.mutate().request(request1).build()))
                    .switchIfEmpty(chain.filter(exchange));
        }

        return chain.filter(exchange);
    }
}
