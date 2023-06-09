package com.snc.gatewayapi.controllers;

import com.snc.gatewayapi.services.BettingService;
import com.snc.snckafkastarter.models.Headers;
import com.snc.snckafkastarter.models.KafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import snc.sncmodels.services.betting.rq.BetRq;
import snc.sncmodels.services.betting.rq.TopUpRq;

@RestController
@RequestMapping("/betting")
@RequiredArgsConstructor
@Slf4j
public class BettingController {
    private final BettingService bettingService;

    @PostMapping("/bet")
    public Mono<ResponseEntity<KafkaMessage>> makeBet(@RequestHeader(Headers.USER_ID) String userId,
                                                      @RequestBody BetRq betRq) {
        betRq.setUserId(userId);
        return bettingService.makeBet(userId, betRq);
    }
}
