package com.snc.gatewayapi.websocket;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SessionStorage {

    private final Map<String, Set<WebSocketSession>> storage = new ConcurrentHashMap<>();

    public Map<String, Set<WebSocketSession>> getStorage() {
        return storage;
    }

    public void addSession(String userId, WebSocketSession session) {
        storage.computeIfAbsent(userId, k -> new HashSet<>());
        storage.get(userId).add(session);
        log.info("USER id {}, add websocket session {}", userId, session);
    }

    public Set<WebSocketSession> getTagretSession(String userId) {
        return storage.get(userId);
    }

    public void removeSession(String userId, String id) {
        Set<WebSocketSession> allUserSessions = storage.get(userId);
        allUserSessions.removeIf(session -> StringUtils.equals(id, session.getId()));
    }
}
