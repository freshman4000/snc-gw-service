package com.snc.gatewayapi.services;

import com.snc.snckafkastarter.models.KafkaMessage;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import snc.sncmodels.services.betting.rq.CreateUserProfileRq;
import snc.sncmodels.services.betting.rq.TopUpRq;

public interface UserService {
    Mono<ResponseEntity<KafkaMessage>> topUpBalance(String userId, TopUpRq topUpRq);

    Mono<ResponseEntity<KafkaMessage>> createProfile(String userId, CreateUserProfileRq rq);
}
