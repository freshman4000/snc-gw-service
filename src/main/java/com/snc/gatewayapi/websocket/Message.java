package com.snc.gatewayapi.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Message {
    private String eventName;
    private String userId;
    private String serviceName;
    private String traceId;
    private String status;
    private String messageBody;
    private String timestamp;
}
