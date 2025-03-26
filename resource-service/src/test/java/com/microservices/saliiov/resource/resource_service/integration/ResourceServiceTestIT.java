package com.microservices.saliiov.resource.resource_service.integration;

import com.microservices.saliiov.resource.resource_service.entity.Resource;
import com.microservices.saliiov.resource.resource_service.exception.ResourceValidationException;
import com.microservices.saliiov.resource.resource_service.repository.ResourceRepository;
import com.microservices.saliiov.resource.resource_service.service.ResourceService;
import com.microservices.saliiov.resource.resource_service.service.impl.ResourceServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "eureka.client.enabled=false")
public class ResourceServiceTestIT {

    @Autowired
    private ResourceRepository resourceRepository;

    private ResourceService resourceService;

    @BeforeEach
    public void setUp() {
        resourceService = new ResourceServiceImpl(resourceRepository);
    }

    @Test
    @SneakyThrows
    public void testCreateResource() {
        Resource resource = new Resource();
        resource.setName("Test Resource");

        Long resourceId = resourceService.createResource(resource);

        Optional<Resource> resourceById = resourceService.getResourceById(resourceId);
        assertTrue(resourceById.isPresent());
        assertEquals(resource.getName(), resourceById.get().getName());
    }

    @Test
    public void testCreateResource_WithEmptyData() {
        assertThrows(ResourceValidationException.class, () ->
                resourceService.createResource(null)
        );
    }

    @Test
    public void testDeleteResourcesByIds() {
        List<Long> ids = createResources();

        List<Resource> deletedResources = resourceService.deleteResourcesByIds(ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",")));

        assertFalse(deletedResources.isEmpty());
        ids.forEach(id -> assertTrue(resourceService.getResourceById(id).isEmpty()));
    }

    private List<Long> createResources() {
        List<Long> ids = new ArrayList<>();
        ids.add(resourceService.createResource(Resource.builder().name("Test #1").build()));
        ids.add(resourceService.createResource(Resource.builder().name("Test #2").build()));
        return ids;
    }

    @Test
    public void testDeleteResourcesByIds_IdsTooLong() {
        String ids = new String(new char[201]).replace('\0', '1');

        assertThrows(ResourceValidationException.class, () ->
                resourceService.deleteResourcesByIds(ids)
        );
    }

    @Test
    public void testDeleteResourcesByIds_IdsAreNotNumber() {
        String ids = "1,2,3,notNumber";

        assertThrows(ResourceValidationException.class, () ->
                resourceService.deleteResourcesByIds(ids)
        );
    }

    @AfterEach
    public void tearDown() {
        resourceRepository.deleteAll();
    }
}