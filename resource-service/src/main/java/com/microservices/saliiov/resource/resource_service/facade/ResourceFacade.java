package com.microservices.saliiov.resource.resource_service.facade;

import com.microservices.saliiov.resource.resource_service.entity.Resource;
import com.microservices.saliiov.resource.resource_service.service.MessageService;
import com.microservices.saliiov.resource.resource_service.service.ResourceService;
import com.microservices.saliiov.resource.resource_service.service.FileStorageService;
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
    private final FileStorageService fileStorageService;
    private final MessageService messageService;
    private final ResourceService resourceService;

    @Transactional
    public Long createResource(byte[] data) {
        log.info("Creating resource");
        String fileName = fileStorageService.uploadFile(data);
        try {
            Long resourceId = resourceService.createResource(Resource.builder()
                    .name(fileName)
                    .build());
            messageService.sendResourceCreatedMessage(resourceId);
            return resourceId;
        } catch (Exception e) {
            log.error("Failed to create resource", e);
            handleFailure(fileName);
            throw e;
        }
    }

    private void handleFailure(String filePath) {
        fileStorageService.deleteFile(filePath);
    }

    public byte[] getResourceDataById(Long id) {
        log.info("Getting resource by id: {}", id);
        Optional<Resource> resourceById = resourceService.getResourceById(id);
        log.info("Resource by id present: {}", resourceById.isPresent());
        return resourceById
                .map(resource -> fileStorageService.downloadFile(resource.getName()))
                .orElse(null);
    }

    public List<Long> deleteResourcesByIds(String id) {
        List<Resource> resources = resourceService.deleteResourcesByIds(id);
        fileStorageService.deleteFiles(resources.stream().map(Resource::getName).toList());
        return resources.stream().map(Resource::getId).toList();
    }
}
