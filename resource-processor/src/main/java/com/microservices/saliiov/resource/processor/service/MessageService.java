package com.microservices.saliiov.resource.processor.service;

import org.springframework.messaging.MessageHandlingException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

/**
 * Service for sending messages
 */
public interface MessageService {
    /**
     * Send message to the destination
     * @param resourceId the id of the created resource
     * @param destination the destination queue
     */
    @Retryable(
            retryFor = {MessageHandlingException.class},
            backoff = @Backoff(delay = 7000))
    void sendMessage(String resourceId, String destination);

    @Recover
    void recover(MessageHandlingException e, String resourceId);
}
