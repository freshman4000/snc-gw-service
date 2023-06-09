package com.snc.gatewayapi.services;

import com.snc.snckafkastarter.converters.KafkaMessageCreator;
import com.snc.snckafkastarter.kafka.MessageService;
import com.snc.snckafkastarter.models.Headers;
import com.snc.snckafkastarter.models.KafkaMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import snc.sncmodels.constants.Events;
import snc.sncmodels.services.betting.rq.BetRq;

import java.util.HashMap;
import java.util.Map;

@Service
public class BettingServiceImpl implements BettingService{
    MessageService messageService;
    KafkaMessageCreator kafkaMessageCreator;

    public BettingServiceImpl(MessageService messageService, KafkaMessageCreator kafkaMessageCreator) {
        this.messageService = messageService;
        this.kafkaMessageCreator = kafkaMessageCreator;
    }

    @Override
    public Mono<ResponseEntity<KafkaMessage>> makeBet(String userId, BetRq betRq) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-TOPIC", "snc-betting-service");
        headers.put(Headers.USER_ID, userId);
        headers.put(Headers.EVENT_NAME, Events.MAKE_MANUAL_BET);
        messageService.sendMessage(kafkaMessageCreator.getMessage(betRq, headers), "snc-betting-service");
        return Mono.just(new ResponseEntity<>(kafkaMessageCreator.getSuccessMessage("Message sent", headers), HttpStatus.OK));
    }
}
