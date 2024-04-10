package com.lc.takeoutApp.websocketHandler;

import com.google.gson.Gson;
import com.lc.takeoutApp.pojo.UserRestaurant;
import com.lc.takeoutApp.pojo.jsonEntity.WebSocketRequest;
import com.lc.takeoutApp.pojo.jsonEntity.WebSocketResponse;
import com.lc.takeoutApp.repository.UserRestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class MyWebSocketHandler implements WebSocketHandler {

    @Autowired
    ReactiveStringRedisTemplate redisTemplate;

    @Autowired
    UserRestaurantRepository userRestaurantRepository;

    @Autowired
    Map<Long, WebSocketSession> wsMap;

    @Autowired
    Gson gson;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        System.out.println(session.getHandshakeInfo().getUri().getQuery());
        System.out.println(1);
//        Flux<WebSocketMessage> output1 = session.receive()
//                .map(webSocketMessage -> gson.fromJson(webSocketMessage.getPayloadAsText(), WebSocketRequest.class))
//                .flatMap(webSocketRequest ->
//                        redisTemplate.opsForValue().get(webSocketRequest.getToken())
//                                .map(Long::parseLong)
//                                .doOnNext(id -> wsMap.put(id, session))
//                                .map(id -> webSocketRequest)
//                ).flatMap(webSocketRequest -> {
//                    switch (webSocketRequest.getType()){
//                        //用户下了单
//                        case 0 -> {
//                            return userRestaurantRepository.findByRestaurantId(webSocketRequest.getTargetId())
//                                    .map(UserRestaurant::getUserId)
//                                    .mapNotNull(id -> wsMap.get(id))
//                                    .doOnNext(webSocketSession -> webSocketSession.send(Mono.just(webSocketSession.textMessage(gson.toJson(new WebSocketResponse(0, null))))).subscribe())
//                                    .then(Mono.empty());
//                        }
//                        default -> {
//                            return Mono.empty();
//                        }
//                    }
//                });
        String[] strings = session.getHandshakeInfo().getUri().getQuery().split("=");
        Long userId = Long.parseLong(strings[1]);
        wsMap.put(userId, session); //发送消息把对应id和session放进map

        Flux<WebSocketMessage> output = session.receive().map(webSocketMessage -> {
            System.out.println(webSocketMessage.getPayloadAsText());
            if(webSocketMessage.getPayloadAsText().equals("\"ping\"")) return session.pongMessage(dataBufferFactory -> dataBufferFactory.wrap("pong".getBytes(StandardCharsets.UTF_8)));
            else return webSocketMessage;
        })
                .doOnTerminate(() -> wsMap.remove(userId));
        return session.send(output);
    }
}
