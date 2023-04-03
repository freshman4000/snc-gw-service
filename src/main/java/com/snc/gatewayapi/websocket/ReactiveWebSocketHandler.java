package com.snc.gatewayapi.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReactiveWebSocketHandler implements WebSocketHandler {
    private final GatewaySocketSubscriber gatewaySocketSubscriber;
    private final MessageSubscriber messageSubscriber;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String query = session.getHandshakeInfo().getUri().getQuery();
        Map<String, String> queryMap = getQueryMap(query);
        String userId = queryMap.getOrDefault("id", "");
        return session.receive()//.timeout(Duration.ofMillis(5000))
                .doOnSubscribe(subscription -> gatewaySocketSubscriber.hookOnSubscribe(userId, session))
                .doOnNext(webSocketMessage -> messageSubscriber.hookOnNext(session))
                .doOnComplete(messageSubscriber::hookOnComplete)
                .doOnError(error -> messageSubscriber.hookOnError(error, userId))
                .then().doFinally(signalType -> gatewaySocketSubscriber.hookOnFinally(userId, session))
                .onErrorResume(TimeoutException.class,
                        timeoutException -> messageSubscriber.closeSessionByInactive(session, userId));
    }

    private Map<String, String> getQueryMap(String queryStr) {
        Map<String, String> queryMap = new HashMap<>();
        if (!StringUtils.isEmpty(queryStr)) {
            String[] queryParam = queryStr.split("&");
            Arrays.stream(queryParam).forEach(s -> {
                String[] kv = s.split("=", 2);
                String value = kv.length == 2 ? kv[1] : "";
                queryMap.put(kv[0], value);
            });
        }
        return queryMap;
    }
}
