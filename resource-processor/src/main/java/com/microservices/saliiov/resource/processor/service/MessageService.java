package com.microservices.saliiov.resource.processor.service;

import org.springframework.messaging.MessageHandlingException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

/**
 * Service for sending messages to the rollback resource queue
 */
public interface MessageService {
    /**
     * Send a message to the resource rollback queue that an error occurred
     * @param resourceId the id of the created resource
     */
    @Retryable(
            retryFor = {MessageHandlingException.class},
            backoff = @Backoff(delay = 7000))
    void sendRollbackMessage(String resourceId);

    @Recover
    void recover(MessageHandlingException e, String resourceId);
}
