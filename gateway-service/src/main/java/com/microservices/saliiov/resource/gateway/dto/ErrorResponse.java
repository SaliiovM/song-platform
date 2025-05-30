package com.microservices.saliiov.resource.gateway.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private int status;
    private String message;
    private String timestamp;
    private String traceId;
}
