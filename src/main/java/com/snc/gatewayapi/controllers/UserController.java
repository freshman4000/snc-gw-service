package com.snc.gatewayapi.controllers;

import com.snc.gatewayapi.services.UserService;
import com.snc.snckafkastarter.models.Headers;
import com.snc.snckafkastarter.models.KafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import snc.sncmodels.services.betting.rq.CreateUserProfileRq;
import snc.sncmodels.services.betting.rq.TopUpRq;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PatchMapping("/balance/topUp")
    public Mono<ResponseEntity<KafkaMessage>> topUpBalance(@RequestHeader(Headers.USER_ID) String userId,
                                                           @RequestBody TopUpRq topUpRq) {
        return userService.topUpBalance(userId, topUpRq);
    }
    @PostMapping("/create")
    public Mono<ResponseEntity<KafkaMessage>> createProfile(@RequestHeader(Headers.USER_ID) String userId,
                                                            @RequestBody CreateUserProfileRq rq) {
        return userService.createProfile(userId, rq);
    }
}
