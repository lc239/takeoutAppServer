package com.lc.takeoutApp.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.handler.DefaultWebFilterChain;
import reactor.core.publisher.Mono;

@Order(1)
@Component
public class WebSocketFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if("/wsp".equals(exchange.getRequest().getPath().pathWithinApplication().toString())){
            return Mono.defer(() -> ((DefaultWebFilterChain) chain).getHandler().handle(exchange));
        }
        return chain.filter(exchange);
    }
}
