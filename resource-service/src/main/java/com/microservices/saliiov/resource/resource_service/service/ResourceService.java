package com.microservices.saliiov.resource.resource_service.service;


import java.util.List;

/**
 * Service for working with resources
 */
public interface ResourceService {
    /**
     * Create resource
     * @param data - resource data
     * @return - id of created resource
     */
    Long createResource(byte[] data);

    /**
     * Get resource data by id
     * @param id - resource id
     * @return - resource data
     */
    byte[] getResourceDataById(Long id);

    /**
     * Delete resources by ids
     * @param ids - ids of resources
     * @return - ids of deleted resources
     */
    List<Long> deleteResourcesByIds(String ids);
}
