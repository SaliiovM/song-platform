package com.microservices.saliiov.resource.processor.service;

import java.util.Optional;

/**
 * Service for working with resource service
 */
public interface ResourceClientService {
    /**
     * Get resource by id
     * @param resourceId - resource id
     * @return - resource data
     */
    Optional<byte[]> getResourceById(String resourceId);

}
