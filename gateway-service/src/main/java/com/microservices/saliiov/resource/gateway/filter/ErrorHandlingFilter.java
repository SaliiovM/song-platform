package com.microservices.saliiov.resource.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.saliiov.resource.gateway.dto.ErrorResponse;
import com.microservices.saliiov.resource.gateway.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Slf4j
@Component
@RequiredArgsConstructor
public class ErrorHandlingFilter extends AbstractGatewayFilterFactory<Object> {

    private final ObjectMapper objectMapper;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> chain.filter(exchange)
                .onErrorResume(t -> {
                    exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    exchange.getResponse().getHeaders().add("Content-Type", "application/json");
                    return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(getBody(t).getBytes())));
                });
    }

    private String getBody(Throwable t)  {
        try {
            return objectMapper.writeValueAsString(ErrorResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(t.getMessage())
                    .timestamp(TimeUtils.getCurrentTimeStamp()));
        }catch (JsonProcessingException e) {
            log.error("Failed to serialize error response", e);
            return StringUtils.EMPTY;
        }
    }

}