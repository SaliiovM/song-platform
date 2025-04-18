package com.microservices.saliiov.resource.resource_service.facade;

import com.microservices.saliiov.resource.resource_service.dto.Storage;
import com.microservices.saliiov.resource.resource_service.entity.Resource;
import com.microservices.saliiov.resource.resource_service.enumeration.StorageType;
import com.microservices.saliiov.resource.resource_service.service.MessageService;
import com.microservices.saliiov.resource.resource_service.service.ResourceService;
import com.microservices.saliiov.resource.resource_service.service.FileStorageService;
import com.microservices.saliiov.resource.resource_service.service.StorageClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceFacade {
    private final StorageClientService storageClientService;
    private final FileStorageService fileStorageService;
    private final MessageService messageService;
    private final ResourceService resourceService;

    @Transactional
    public Long createResource(byte[] data) {
        log.info("Creating resource");
        Storage storage = storageClientService.getStorageByType(StorageType.STAGING.name());
        log.debug("Storage: {}", storage);
        String key = fileStorageService.uploadFile(data, storage);
        log.debug("File name: {}", key);
        return createResource(key, storage);
    }

    private Long createResource(String key, Storage storage) {
        try {
            Long resourceId = resourceService.createResource(Resource.builder()
                    .key(key)
                    .state(storage.getStorageType())
                    .build());
            messageService.sendResourceCreatedMessage(resourceId);
            return resourceId;
        } catch (Exception e) {
            log.error("Failed to create resource", e);
            handleFailure(key);
            throw e;
        }
    }

    private void handleFailure(String filePath) {
        fileStorageService.deleteFile(filePath, storageClientService.getStorageByType(StorageType.PERMANENT.name()));
    }

    public byte[] getResourceDataById(Long id) {
        log.info("Getting resource by id: {}", id);
        Optional<Resource> resourceById = resourceService.getResourceById(id);
        log.info("Resource by id present: {}", resourceById.isPresent());
        return resourceById
                .map(resource -> fileStorageService.downloadFile(resource.getKey(),
                        storageClientService.getStorageByType(StorageType.PERMANENT.name())))
                .orElse(null);
    }

    public List<Long> deleteResourcesByIds(String id) {
        List<Resource> resources = resourceService.deleteResourcesByIds(id);
        fileStorageService.deleteFiles(resources.stream().map(Resource::getKey).toList(),
                storageClientService.getStorageByType(StorageType.PERMANENT.name()));
        return resources.stream().map(Resource::getId).toList();
    }

    public void processSuccess(String resourceID) {
        log.info("Processing success for resource: {}", resourceID);
        try {
            Resource resource = getResource(resourceID);
            Storage currentStorage = storageClientService.getStorageByType(resource.getState());
            Storage permanentStorage = storageClientService.getStorageByType(StorageType.PERMANENT.name());
            String newKey = fileStorageService.moveFile(resource.getKey(), currentStorage, permanentStorage);
            updateResource(resource, newKey);
            log.info("Resource moved to permanent storage: {}, new key: {}", permanentStorage, newKey);
        } catch (Exception e) {
            log.error("Failed to process success for resource: {}", resourceID, e);
        }

    }

    private void updateResource(Resource resource, String newKey) {
        resource.setKey(newKey);
        resource.setState(StorageType.PERMANENT.name());
        resourceService.createResource(resource);
    }

    private Resource getResource(String resourceID) {
        return resourceService.getResourceById(Long.valueOf(resourceID))
                .orElseThrow(() -> new RuntimeException("Resource not found"));
    }
}
