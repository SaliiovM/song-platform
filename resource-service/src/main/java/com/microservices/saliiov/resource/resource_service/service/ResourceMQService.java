package com.microservices.saliiov.resource.resource_service.service;

/**
 * Service for sending messages to the resource queue
 */
public interface ResourceMQService {
    /**
     * Send a message to the resource queue that a resource has been created
     * @param resourceId the id of the created resource
     */
    void sendResourceCreatedMessage(Long resourceId);
}
