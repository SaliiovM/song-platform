package com.microservices.saliiov.resource.storage.service;

import com.microservices.saliiov.resource.storage.dto.CreateStorageDto;
import com.microservices.saliiov.resource.storage.dto.GetStorageDto;

import java.util.List;

/**
 * Service for Storage entity
 */
public interface StorageService {
    Long createStorage(CreateStorageDto dto);
    List<GetStorageDto> getAllStorages();
    void deleteStorages(String ids);
    GetStorageDto getStorageByType(String type);
}
