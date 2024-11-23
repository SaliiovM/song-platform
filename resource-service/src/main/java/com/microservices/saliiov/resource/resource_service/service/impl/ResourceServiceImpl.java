package com.microservices.saliiov.resource.resource_service.service.impl;

import com.microservices.saliiov.resource.resource_service.entity.Resource;
import com.microservices.saliiov.resource.resource_service.exception.ResourceValidationException;
import com.microservices.saliiov.resource.resource_service.service.ResourceService;
import com.microservices.saliiov.resource.resource_service.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;

    @Override
    @Transactional
    public Long createResource(Resource resource) {
        if (Objects.isNull(resource) || StringUtils.isBlank(resource.getName())) {
            throw new ResourceValidationException("Resource data is empty");
        }

        try {
            return resourceRepository.save(resource).getId();
        } catch (Exception e) {
            log.error("Error creating resource: ", e);
            throw new ResourceValidationException("Error creating resource");
        }
    }

    @Override
    public Optional<Resource> getResourceById(Long id) {
        return Optional.ofNullable(id)
                .map(resourceRepository::findById)
                .orElseThrow(() -> new ResourceValidationException("id is required"));
    }

    @Override
    public List<Resource> deleteResourcesByIds(String ids) {
        validate(ids);
        List<Long> idsToDelete = Arrays.stream(StringUtils.split(ids, ",")).map(Long::valueOf).toList();
        Iterable<Resource> allById = resourceRepository.findAllById(idsToDelete);
        resourceRepository.deleteAllById(idsToDelete);
        return new ArrayList<>((Collection<Resource>) allById);
    }

    private void validate(String ids) {
        if (StringUtils.length(ids) > 200) {
            throw new ResourceValidationException("Resource ids are too long");
        }

        if (Arrays.stream(StringUtils.split(ids, ",")).anyMatch(id -> !NumberUtils.isDigits(id))) {
            throw new ResourceValidationException("Resource ids are required and must be numbers");
        }
    }
}
