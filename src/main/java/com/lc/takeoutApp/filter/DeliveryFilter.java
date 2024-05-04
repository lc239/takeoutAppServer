package com.lc.takeoutApp.filter;

import com.lc.takeoutApp.pojo.UserDelivery;
import com.lc.takeoutApp.pojo.UserRestaurant;
import com.lc.takeoutApp.repository.UserDeliveryRepository;
import com.lc.takeoutApp.repository.UserRestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

@Order(5)
@Component
public class DeliveryFilter implements WebFilter {

    @Autowired
    UserDeliveryRepository userDeliveryRepository;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        PathPattern deliveryPattern=new PathPatternParser().parse("/delivery/**");
        ServerHttpRequest request=exchange.getRequest();

        PathContainer pathWithinApplication = request.getPath().pathWithinApplication();

        if (
                deliveryPattern.matches(pathWithinApplication)
        ){
            return userDeliveryRepository.findByUserId(Long.valueOf(request.getHeaders().get("userId").get(0)))
                    .map(UserDelivery::getDeliveryManId)
                    .map(restaurantId -> request.mutate().header("deliveryId", String.valueOf(restaurantId)).build())
                    .flatMap(request1 -> chain.filter(exchange.mutate().request(request1).build()))
                    .switchIfEmpty(chain.filter(exchange));
        }

        return chain.filter(exchange);
    }
}
