package com.microservices.saliiov.resource.processor.exception;

public class ResourceProcessorException extends RuntimeException {
    public ResourceProcessorException(String message) {
        super(message);
    }
    public ResourceProcessorException(String message, Throwable cause) {
        super(message, cause);
    }
}
