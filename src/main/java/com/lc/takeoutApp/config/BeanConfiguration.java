package com.lc.takeoutApp.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lc.takeoutApp.gsonAdapter.InstantAdapter;
import com.lc.takeoutApp.websocketHandler.MyWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class BeanConfiguration {

    @Bean
    public WebSocketHandlerAdapter handlerAdapter(){
        return new WebSocketHandlerAdapter();
    }

    @Bean
    public HandlerMapping handlerMapping(WebSocketHandler webSocketHandler){
        Map<String, WebSocketHandler> urlMap = new HashMap<>();
        urlMap.put("/wsp", webSocketHandler);
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
        mapping.setUrlMap(urlMap);
        return mapping;
    }

    //可以考虑用户id映射到多个连接
    @Bean
    Map<Long, WebSocketSession> wsMap(){
        return new HashMap<>();
    }

    @Bean
    public Gson gson(){
        Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
        return gson;
    }
}
