package com.microservices.saliiov.resource.processor.service.impl;

import com.microservices.saliiov.resource.processor.client.ResourceServiceClient;
import com.microservices.saliiov.resource.processor.exception.ClientRetryException;
import com.microservices.saliiov.resource.processor.exception.ResourceProcessorException;
import com.microservices.saliiov.resource.processor.service.ResourceClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceClientServiceImpl implements ResourceClientService {

    private final ResourceServiceClient resourceServiceClient;

    @Retryable(
            retryFor = {ClientRetryException.class},
            backoff = @Backoff(delay = 12000)
    )
    @Override
    public Optional<byte[]> getResourceById(String resourceId) {
        if (Objects.isNull(resourceId)) {
            return Optional.empty();
        }

        try {
            ResponseEntity<ByteArrayResource> resourceByIdEntity = resourceServiceClient.getResourceById(Long.valueOf(resourceId));
            return Optional.of(Objects.requireNonNull(resourceByIdEntity.getBody()).getByteArray());
        } catch (Exception e) {
            log.error("Failed to get resource by id. Resource id: {}", resourceId, e);
            throw new ClientRetryException("Failed to get resource by id");
        }
    }


    @Recover
    public void recover(ClientRetryException e) {
        log.error("Failed to send a request after number of retries", e);
        throw new ResourceProcessorException("Failed to send a request");
    }
}
