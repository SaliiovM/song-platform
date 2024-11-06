package com.microservices.saliiov.resource.resource_service.exception;

public class S3ProcessingException extends RuntimeException {
    public S3ProcessingException(String message) {
        super(message);
    }
}
