package com.microservices.saliiov.resource.resource_service.service.impl;

import com.microservices.saliiov.resource.resource_service.producer.ResourceProducer;
import com.microservices.saliiov.resource.resource_service.service.ResourceMQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceMQServiceImpl implements ResourceMQService {

    private final ResourceProducer resourceProducer;

    @Override
    public void sendResourceCreatedMessage(Long resourceId) {
        resourceProducer.sendResourceId(resourceId);
    }
}
