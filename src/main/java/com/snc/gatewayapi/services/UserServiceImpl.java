package com.snc.gatewayapi.services;

import com.snc.snckafkastarter.converters.KafkaMessageCreator;
import com.snc.snckafkastarter.kafka.MessageService;
import com.snc.snckafkastarter.models.Headers;
import com.snc.snckafkastarter.models.KafkaMessage;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import snc.sncmodels.constants.Events;
import snc.sncmodels.services.betting.rq.CreateUserProfileRq;
import snc.sncmodels.services.betting.rq.TopUpRq;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    MessageService messageService;
    KafkaMessageCreator kafkaMessageCreator;

    @Override
    public Mono<ResponseEntity<KafkaMessage>> topUpBalance(String userId, TopUpRq topUpRq) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-TOPIC", "snc-user-service");
        headers.put(Headers.USER_ID, userId);
        headers.put(Headers.EVENT_NAME, Events.TOP_UP_BALANCE);
        messageService.sendMessage(kafkaMessageCreator.getMessage(topUpRq, headers), "snc-user-service");
        return Mono.just(new ResponseEntity<>(kafkaMessageCreator.getSuccessMessage("Message sent", headers), HttpStatus.OK));
    }

    @Override
    public Mono<ResponseEntity<KafkaMessage>> createProfile(String userId, CreateUserProfileRq rq) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-TOPIC", "snc-user-service");
        headers.put(Headers.USER_ID, userId);
        headers.put(Headers.EVENT_NAME, Events.CREATE_USER_PROFILE);
        messageService.sendMessage(kafkaMessageCreator.getMessage(rq, headers), "snc-user-service");
        return Mono.just(new ResponseEntity<>(kafkaMessageCreator.getSuccessMessage("Message sent", headers), HttpStatus.OK));
    }
}
