package com.lc.takeoutApp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.lc.takeoutApp.gsonAdapter.InstantAdapter;
import com.lc.takeoutApp.pojo.jsonEntity.Category;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;

public class CommonTests {

}
