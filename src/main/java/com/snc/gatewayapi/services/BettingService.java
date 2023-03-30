package com.snc.gatewayapi.services;


import com.snc.snckafkastarter.models.KafkaMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import snc.sncmodels.services.betting.rq.BetRq;

@Service
public interface BettingService {
    public Mono<ResponseEntity<KafkaMessage>> makeBet(String userId, BetRq betRq);
}
