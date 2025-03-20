package com.microservices.saliiov.resource.resource_service.service;


import com.microservices.saliiov.resource.resource_service.entity.Resource;

import java.util.List;
import java.util.Optional;

/**
 * Service for working with resources
 */
public interface ResourceService {
    /**
     * Create resource
     * @param resource - resource data
     * @return - id of created resource
     */
    Long createResource(Resource resource);

    /**
     * Get resource data by id
     * @param id - resource id
     * @return - optional of resource
     */
    Optional<Resource> getResourceById(Long id);

    /**
     * Delete resources by ids
     *
     * @param ids - ids of resources
     * @return - ids of deleted resources
     */
    List<Resource> deleteResourcesByIds(String ids);
}
