package com.snc.gatewayapi.services;

import com.snc.gatewayapi.models.BetRq;
import com.snc.snckafkastarter.models.KafkaMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface BettingService {
    public Mono<ResponseEntity<KafkaMessage>> makeBet(String userId, BetRq betRq);
}
