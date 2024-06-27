package com.microservices.saliiov.resource.resource_service.exception;

public class ResourceValidationException extends RuntimeException {
    public ResourceValidationException(String message) {
        super(message);
    }
}
