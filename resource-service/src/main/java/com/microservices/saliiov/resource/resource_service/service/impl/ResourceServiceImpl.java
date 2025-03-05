package com.microservices.saliiov.resource.resource_service.service.impl;

import com.microservices.saliiov.resource.resource_service.entity.Resource;
import com.microservices.saliiov.resource.resource_service.exception.ResourceValidationException;
import com.microservices.saliiov.resource.resource_service.exception.S3ProcessingException;
import com.microservices.saliiov.resource.resource_service.service.ResourceService;
import com.microservices.saliiov.resource.resource_service.repository.ResourceRepository;
import com.microservices.saliiov.resource.resource_service.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;
    private final S3Service s3Service;

    @Override
    @Transactional
    public Long createResource(byte[] data) {
        if (ArrayUtils.isEmpty(data)) {
            throw new ResourceValidationException("Audio data is empty");
        }

        try {
            return saveResource(data);
        } catch (HttpClientErrorException | S3ProcessingException e) {
            log.error("Error creating resource: ", e);
            throw new ResourceValidationException("Error creating resource");
        }
    }

    @Override
    public byte[] getResourceDataById(Long id) {
        return Optional.ofNullable(id).map(resourceRepository::findById)
                .orElseThrow(() -> new ResourceValidationException("Resource id is required"))
                .map(resource -> s3Service.downloadFile(resource.getName()))
                .orElse(null);
    }

    @Override
    public List<Long> deleteResourcesByIds(String ids) {
        validate(ids);
        List<Long> idsToDelete = Arrays.stream(StringUtils.split(ids, ",")).map(Long::valueOf).toList();
        resourceRepository.deleteAllById(idsToDelete);
        return idsToDelete;
    }

    private void validate(String ids) {
        if (StringUtils.length(ids) > 200) {
            throw new ResourceValidationException("Resource ids are too long");
        }

        if (Arrays.stream(StringUtils.split(ids, ",")).anyMatch(id -> !NumberUtils.isDigits(id))) {
            throw new ResourceValidationException("Resource ids are required and must be numbers");
        }
    }

    private Long saveResource(byte[] data) {
        String fileName = s3Service.uploadFile(data);
        Resource resource = new Resource();
        resource.setName(fileName);
        return resourceRepository.save(resource).getId();
    }

}
