package com.snc.gatewayapi.websocket;

public class SendMessageParseException extends RuntimeException {
    private static final String MESSAGE = "Error when convert object [ %s ] to json";
    public SendMessageParseException(Object object, Exception cause) {
        super(String.format(MESSAGE, object), cause);
    }
}
