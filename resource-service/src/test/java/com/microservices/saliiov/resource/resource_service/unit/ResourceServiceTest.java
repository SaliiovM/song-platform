package com.microservices.saliiov.resource.resource_service.unit;

import com.microservices.saliiov.resource.resource_service.entity.Resource;
import com.microservices.saliiov.resource.resource_service.exception.ResourceValidationException;
import com.microservices.saliiov.resource.resource_service.repository.ResourceRepository;
import com.microservices.saliiov.resource.resource_service.service.ResourceService;
import com.microservices.saliiov.resource.resource_service.service.impl.ResourceServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ResourceServiceTest {

    @Mock
    private ResourceRepository resourceRepository;

    private ResourceService resourceService;

    @BeforeEach
    public void setUp() {
        resourceService = new ResourceServiceImpl(resourceRepository);
    }

    @Test
    @SneakyThrows
    public void testCreateResource() {
        Long expectedId = 1L;
        Resource mockResource = mock(Resource.class);
        when(mockResource.getId()).thenReturn(expectedId);
        when(mockResource.getKey()).thenReturn("mockData");
        when(resourceRepository.save(any(Resource.class))).thenReturn(mockResource);

        Long resourceId = resourceService.createResource(mockResource);

        verify(resourceRepository, times(1)).save(any(Resource.class));
        assertEquals(expectedId, resourceId);
    }

    @Test
    public void testCreateResource_WithEmptyData() {
        assertThrows(ResourceValidationException.class, () ->
                resourceService.createResource(null)
        );
    }

    @Test
    @SneakyThrows
    public void testCreateResource_WithError() {
        assertThrows(ResourceValidationException.class, () ->
                resourceService.createResource(new Resource())
        );
    }

    @Test
    public void testGetResourceById() {
        String expectedName = "mockData";
        Resource mockResource = mock(Resource.class);
        when(mockResource.getKey()).thenReturn(expectedName);
        when(resourceRepository.findById(anyLong())).thenReturn(Optional.of(mockResource));

        Optional<Resource> actualData = resourceService.getResourceById(1L);

        verify(resourceRepository, times(1)).findById(anyLong());
        assertTrue(actualData.isPresent());
        assertEquals(expectedName, actualData.get().getKey());
    }

    @Test
    public void testDeleteResourcesByIds() {
        String ids = "1,2,3";

        assertDoesNotThrow(() -> resourceService.deleteResourcesByIds(ids));
        verify(resourceRepository, times(1)).deleteAllById(anyList());
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
}