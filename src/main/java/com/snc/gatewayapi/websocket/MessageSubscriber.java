package com.snc.gatewayapi.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snc.snckafkastarter.models.Headers;
import com.snc.snckafkastarter.models.KafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.socket.CloseStatus;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageSubscriber {

    private final SessionStorage sessionStorage;

    public void hookOnComplete() {
        //super.hookOnComplete();
        System.out.println("hookOnComplete");
    }

    public void hookOnError(Throwable throwable, String userId) {
        log.error("HookOnError userId = {}, error : {}", userId, throwable);
    }

    public void hookOnNext(WebSocketSession session) {
        session.send(Mono.just(session.textMessage("0xA"))).subscribe();
    }

    public void sendMessage(KafkaMessage kafkaMessage) {

        String userId = kafkaMessage.getHeaders().get(Headers.USER_ID);
        Message message = createWsMessage(kafkaMessage);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent;
        try {
            jsonContent = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException exception) {
            throw new SendMessageParseException(message, exception);
        }
        Set<WebSocketSession> sessions = sessionStorage.getTagretSession(userId);

        if (sessions == null || CollectionUtils.isEmpty(sessions)) {
            log.error("The connection was not opened. Message was not sent, traceId = {}, userId = {}, eventName = {}",
                    message.getTraceId(), message.getUserId(), message.getEventName());
        } else {
            sessions.stream()
                    .filter(WebSocketSession::isOpen)
                    .forEach(session -> {
                        WebSocketMessage target = session.textMessage(jsonContent);
                        session.send(Mono.just(target)).subscribe();
                        log.info("[traceId = {}]Message was sent to websocket session. UserId = {}, eventName = {}",
                                message.getTraceId(), message.getUserId(), message.getEventName());
                    });
        }
    }

    public Message createWsMessage(KafkaMessage kafkaMessage) {
        return Message.builder()
                .timestamp(ZonedDateTime.now(Clock.systemUTC()).format( DateTimeFormatter.ISO_ZONED_DATE_TIME))
                .eventName(kafkaMessage.getHeaders().get(Headers.EVENT_NAME))
                .userId(kafkaMessage.getHeaders().get(Headers.USER_ID))
                .status(kafkaMessage.getHeaders().get(Headers.STATUS))
                .serviceName(kafkaMessage.getHeaders().get(Headers.APPLICATION))
                .traceId(kafkaMessage.getHeaders().get(Headers.X_TRACE_ID))
                .messageBody(kafkaMessage.getMessageBody())
                .build();
    }

    public Mono<? extends Void> closeSessionByInactive(WebSocketSession session, String userId) {
        log.info("Session will close by timeout, userId = {}", userId);
        sessionStorage.removeSession(userId, session.getId());
        return session.close(CloseStatus.NORMAL);
    }
}
