package com.microservices.saliiov.resource.resource_service.exception;

import com.microservices.saliiov.resource.resource_service.utils.TimeUtils;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ResourceResponseEntityExceptionHandler {

    private final Tracer tracer;

    @ExceptionHandler(value = {ResourceValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseError> handleResourceCreationException(ResourceValidationException e) {
        log.error("ResourceCreationException", e);
        return ResponseEntity.badRequest()
                .body(ResponseError.builder().
                        status(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage())
                        .timestamp(TimeUtils.getCurrentTimeStamp())
                        .traceId(getTraceId())
                        .build());
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseError> handleException(Exception e) {
        log.error("Exception", e);
        return ResponseEntity.internalServerError()
                .body(ResponseError.builder().
                        status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(e.getMessage())
                        .timestamp(TimeUtils.getCurrentTimeStamp())
                        .traceId(getTraceId())
                        .build());
    }

    private String getTraceId() {
        return Optional.ofNullable(tracer.currentSpan())
                .map(Span::context)
                .map(TraceContext::traceId)
                .orElse("N/A");
    }
}
