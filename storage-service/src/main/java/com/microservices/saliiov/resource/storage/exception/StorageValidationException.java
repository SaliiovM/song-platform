package com.microservices.saliiov.resource.storage.exception;

public class StorageValidationException extends RuntimeException {
    public StorageValidationException(String message) {
        super(message);
    }
}
