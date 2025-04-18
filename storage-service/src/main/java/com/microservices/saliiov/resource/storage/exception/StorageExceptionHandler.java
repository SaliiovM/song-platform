package com.microservices.saliiov.resource.storage.exception;

import com.microservices.saliiov.resource.storage.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class StorageExceptionHandler {

    @ExceptionHandler(value = {StorageValidationException.class})
    public ResponseEntity<ResponseError> handleStorageValidationException(StorageValidationException e) {
        log.error("StorageValidationException", e);
        return ResponseEntity.badRequest()
                .body(getResponseError(e, HttpStatus.SC_BAD_REQUEST));
    }

    @ExceptionHandler(value = {StorageNotFoundException.class})
    public ResponseEntity<ResponseError> handleStorageNotFoundException(StorageNotFoundException e) {
        log.error("StorageNotFoundException", e);
        return ResponseEntity.badRequest()
                .body(getResponseError(e, HttpStatus.SC_NOT_FOUND));
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ResponseError> handleException(Exception e) {
        log.error("Exception", e);
        return ResponseEntity.internalServerError()
                .body(getResponseError(e, HttpStatus.SC_INTERNAL_SERVER_ERROR));
    }

    private ResponseError getResponseError(Exception e, int status) {
        return ResponseError.builder()
                .status(status)
                .message(e.getMessage())
                .timestamp(TimeUtils.getCurrentTimeStamp())
                .build();
    }
}
