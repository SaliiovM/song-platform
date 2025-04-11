package com.microservices.saliiov.resource.resource_service.service.impl;

import com.microservices.saliiov.resource.resource_service.client.StorageServiceClient;
import com.microservices.saliiov.resource.resource_service.config.StubStorage;
import com.microservices.saliiov.resource.resource_service.dto.Storage;
import com.microservices.saliiov.resource.resource_service.exception.ResourceValidationException;
import com.microservices.saliiov.resource.resource_service.service.StorageClientService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageClientServiceImpl implements StorageClientService {

    private final StorageServiceClient client;
    private final StubStorage stubStorage;

    @Override
    @CircuitBreaker(name = "storageService", fallbackMethod = "getStorageByTypeFallback")
    public Storage getStorageByType(String type) {
        if (StringUtils.isBlank(type)) {
            throw new ResourceValidationException("Failed to send a request. Storage type is empty");
        }

        ResponseEntity<Storage> song = client.getStorageByType(type);
        return song.getBody();
    }

    public Storage getStorageByTypeFallback(String type, Throwable t) {
        log.error("Failed to send a request. Storage type: {}. Error: {}", type, t.getMessage());
        Storage stub = stubStorage.getStub();
        log.info("Returning stub storage: {}", stub);
        return stub;
    }
}
