package com.microservices.saliiov.resource.resource_service.service;

import com.microservices.saliiov.resource.resource_service.dto.Storage;

/**
 * Service for communicating with the storage service
 */
public interface StorageClientService {
    Storage getStorageByType(String type);
}
