package com.microservices.saliiov.resource.resource_service.exception;

public class MessageProcessingException extends RuntimeException {
    public MessageProcessingException(String message) {
        super(message);
    }
}
