package com.microservices.saliiov.resource.resource_service.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseError {
    private int status;
    private String message;
    private String timestamp;
    private String traceId;
}
