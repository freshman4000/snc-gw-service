package com.snc.gatewayapi.handlers;

import com.snc.gatewayapi.websocket.MessageSubscriber;
import com.snc.snckafkastarter.JsonConverter;
import com.snc.snckafkastarter.event.annotation.Event;
import com.snc.snckafkastarter.kafka.MessageService;
import com.snc.snckafkastarter.models.Headers;
import com.snc.snckafkastarter.models.KafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import snc.sncmodels.constants.Events;

import java.util.Map;

@com.snc.snckafkastarter.event.annotation.EventHandler
@RequiredArgsConstructor
@Slf4j
public class EventHandler {

    private final MessageSubscriber messageSubscriber;

    @Event(Events.APPLY_BET_RESULT)
    public void processEvent(KafkaMessage kafkaMessage) {
        Map<String, String> headers = kafkaMessage.getHeaders();
        try {
            log.info("[traceId = {}]Received  message {}", headers.get(Headers.X_TRACE_ID), kafkaMessage);
            messageSubscriber.sendMessage(kafkaMessage);
        } catch (Exception e) {
        log.error(e.getMessage());
        }
    }
}
