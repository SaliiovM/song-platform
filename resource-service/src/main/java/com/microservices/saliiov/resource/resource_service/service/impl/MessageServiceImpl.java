package com.microservices.saliiov.resource.resource_service.service.impl;

import com.microservices.saliiov.resource.resource_service.dto.ResourceMessage;
import com.microservices.saliiov.resource.resource_service.exception.MessageProcessingException;
import com.microservices.saliiov.resource.resource_service.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    @Value("${resource.destination.created}")
    private String resourceCreatedDestination;

    private final StreamBridge streamBridge;

    @Override
    public void sendResourceCreatedMessage(Long resourceId) {
        log.info("Sending Resource ID: {} to the resource destination.", resourceId);
        streamBridge.send(resourceCreatedDestination, ResourceMessage.builder().id(String.valueOf(resourceId)).build());
    }

    @Override
    public void recover(MessageHandlingException e, Long resourceId) {
        log.info("Retry tries are exceeded for Resource ID: {}.", resourceId, e);
        throw new MessageProcessingException("Failed to send Resource ID: " + resourceId);
    }
}
