package com.microservices.saliiov.resource.resource_service.facade;

import com.microservices.saliiov.resource.resource_service.entity.Resource;
import com.microservices.saliiov.resource.resource_service.service.ResourceMQService;
import com.microservices.saliiov.resource.resource_service.service.ResourceService;
import com.microservices.saliiov.resource.resource_service.service.ResourceFileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceFacade {
    private final ResourceFileStorageService resourceFileStorageService;
    private final ResourceMQService resourceMQService;
    private final ResourceService resourceService;

    public Long createResource(byte[] data) {
        log.info("Creating resource");
        String fileName = resourceFileStorageService.uploadFile(data);

        Long resourceId = resourceService.createResource(Resource.builder()
                .name(fileName)
                .build());

        resourceMQService.sendResourceCreatedMessage(resourceId);

        return resourceId;
    }

    public byte[] getResourceDataById(Long id) {
        return resourceService.getResourceById(id)
                .map(resource -> resourceFileStorageService.downloadFile(resource.getName()))
                .orElse(null);
    }

    public List<Long> deleteResourcesByIds(String id) {
        List<Resource> resources = resourceService.deleteResourcesByIds(id);
        resourceFileStorageService.deleteFiles(resources.stream().map(Resource::getName).toList());
        return resources.stream().map(Resource::getId).toList();
    }
}
