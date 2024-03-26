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
public class StaticResourceFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        System.out.println(0);
        System.out.println(exchange.getRequest().getPath().pathWithinApplication());
        if (exchange.getRequest().getURI().getPath().equals("/")) {
            return Mono.defer(() -> ((DefaultWebFilterChain) chain).getHandler().handle(
                    exchange.mutate().request(exchange.getRequest().mutate().path("/index.html").build()).build()
            ));
        }
        //静态资源直接返回
        if(
                exchange.getRequest().getPath().pathWithinApplication().toString().endsWith(".js") ||
                        exchange.getRequest().getPath().pathWithinApplication().toString().endsWith(".css") ||
                        exchange.getRequest().getPath().pathWithinApplication().toString().endsWith(".html") ||
                        exchange.getRequest().getPath().pathWithinApplication().toString().endsWith(".ico")
        ){
            return Mono.defer(() -> ((DefaultWebFilterChain) chain).getHandler().handle(exchange));
        }
        return chain.filter(exchange);
    }
}
