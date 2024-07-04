package com.microservices.saliiov.resource.resource_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ResourceResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ResourceValidationException.class})
    public ResponseEntity<ResponseError> handleResourceCreationException(ResourceValidationException e) {
        log.error("ResourceCreationException", e);
        return ResponseEntity.badRequest()
                .body(ResponseError.builder()
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ResponseError> handleException(Exception e) {
        log.error("Exception", e);
        return ResponseEntity.internalServerError()
                .body(ResponseError.builder()
                        .message(e.getMessage())
                        .build());
    }
}
