package com.microservices.saliiov.resource.resource_service.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ResourceProducer {

    @Value("${resource.queue}")
    private String resourceQueue;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Retryable(
            retryFor = {AmqpConnectException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 7000))
    public void sendResourceId(Long resourceId) {
        rabbitTemplate.convertAndSend(resourceQueue, String.valueOf(resourceId));
        log.info("Sent Resource ID: {}", resourceId);
    }

    @Recover
    public void recover(AmqpConnectException e, Long resourceId) {
        log.error("Failed to send Resource ID: {}", resourceId, e);
    }
}
