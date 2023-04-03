package com.snc.gatewayapi.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class GatewaySocketSubscriber {
    private final SessionStorage sessionStorage;

    public void hookOnSubscribe(String userId, WebSocketSession session) {
        try {
            sessionStorage.addSession(userId, session);
            WebSocketMessage message = session.textMessage("User with id " + userId + " established connection. Session id is " + session.getId());
            session.send(Mono.just(message)).subscribe();
        } catch (Exception e) {
            log.error("Error userId {}, Message -- {}:", userId, e.getMessage());
        }

    }
    public void hookOnFinally(String userId, WebSocketSession session) {
        sessionStorage.removeSession(userId, session.getId());
        log.info("Remove session  id = {} for userId = {}", session.getId(), userId);
        log.info(sessionStorage.getStorage().toString());
    }
}
