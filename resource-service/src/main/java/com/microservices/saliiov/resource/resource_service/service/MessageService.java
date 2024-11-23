package com.microservices.saliiov.resource.resource_service.service;

import org.springframework.messaging.MessageHandlingException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

/**
 * Service for sending messages to the resource queue
 */
public interface MessageService {
    /**
     * Send a message to the resource queue that a resource has been created
     * @param resourceId the id of the created resource
     */
    @Retryable(
            retryFor = {MessageHandlingException.class},
            backoff = @Backoff(delay = 7000))
    void sendResourceCreatedMessage(Long resourceId);

    @Recover
    void recover(MessageHandlingException e, Long resourceId);
}
