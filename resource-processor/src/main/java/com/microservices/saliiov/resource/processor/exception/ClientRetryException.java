package com.microservices.saliiov.resource.processor.exception;

public class ClientRetryException extends RuntimeException {
    public ClientRetryException(String message) {
        super(message);
    }
}
