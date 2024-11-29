package com.microservices.saliiov.resource.processor.service.impl;

import com.microservices.saliiov.resource.processor.client.ResourceServiceClient;
import com.microservices.saliiov.resource.processor.exception.ClientRetryException;
import com.microservices.saliiov.resource.processor.exception.ResourceNotFoundException;
import com.microservices.saliiov.resource.processor.exception.ResourceProcessorException;
import com.microservices.saliiov.resource.processor.service.ResourceClientService;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
            if (Objects.nonNull(resourceByIdEntity.getBody())) {
                return Optional.of(resourceByIdEntity.getBody().getByteArray());
            } else {
                log.error("Failed to get resource by id. Resource id: {}", resourceId);
                return Optional.empty();
            }
        } catch (RetryableException e) {
            log.error("Failed to get resource by id. Resource id: {}", resourceId, e);
            throw new ClientRetryException("Failed to get resource by id");
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found. Resource id: {}", resourceId, e);
            return Optional.empty();
        }
    }

    @Retryable(
            retryFor = {ClientRetryException.class},
            backoff = @Backoff(delay = 2000)
    )
    @Override
    public void deleteResourceById(String resourceId) {
        if (StringUtils.isNotBlank(resourceId)) {
            resourceServiceClient.deleteResources(resourceId);
            log.info("Deleted resource with id: {}", resourceId);
        }
    }

    @Recover
    public void recover(ClientRetryException e) {
        log.error("Failed to send a request after number of retries", e);
        throw new ResourceProcessorException("Failed to send a request");
    }
}
