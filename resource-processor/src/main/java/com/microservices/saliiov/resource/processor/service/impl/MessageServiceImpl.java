package com.microservices.saliiov.resource.processor.service.impl;

import com.microservices.saliiov.resource.processor.dto.ResourceMessage;
import com.microservices.saliiov.resource.processor.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final StreamBridge streamBridge;

    public void sendMessage(String resourceId, String destination) {
        log.info("Sending Resource ID: {} to the resource destination: {}", resourceId, destination);
        streamBridge.send(destination, ResourceMessage.builder().id(resourceId).build());
    }

    @Override
    public void recover(MessageHandlingException e, String resourceId) {
        log.error("Retry tries are exceeded for Resource ID: {}.", resourceId, e);
    }
}
