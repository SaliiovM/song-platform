package com.microservices.saliiov.resource.resource_service.service.impl;

import com.microservices.saliiov.resource.resource_service.entity.Resource;
import com.microservices.saliiov.resource.resource_service.exception.ResourceValidationException;
import com.microservices.saliiov.resource.resource_service.exception.S3ProcessingException;
import com.microservices.saliiov.resource.resource_service.repository.ResourceRepository;
import com.microservices.saliiov.resource.resource_service.service.S3Service;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest(properties = "eureka.client.enabled=false")
public class ResourceServiceImplTest {

    @MockBean
    private S3Service s3Service;

    @MockBean
    private ResourceRepository resourceRepository;

    @Autowired
    private ResourceServiceImpl resourceService;

    @Test
    @SneakyThrows
    public void testCreateResource() {
        byte[] mockData = "mockData".getBytes();
        Long expectedId = 1L;
        Resource mockResource = mock(Resource.class);
        when(mockResource.getId()).thenReturn(expectedId);
        when(resourceRepository.save(any(Resource.class))).thenReturn(mockResource);

        Long resourceId = resourceService.createResource(mockData);

        verify(resourceRepository, times(1)).save(any(Resource.class));
        assertEquals(expectedId, resourceId);
    }

    @Test
    public void testCreateResource_WithEmptyData() {
        byte[] emptyData = new byte[0];

        assertThrows(ResourceValidationException.class, () ->
                resourceService.createResource(emptyData)
        );
    }

    @Test
    @SneakyThrows
    public void testCreateResource_WithError() {
        byte[] mockData = "mockData".getBytes();
        when(s3Service.uploadFile(mockData)).thenThrow(new S3ProcessingException("Error"));

        assertThrows(ResourceValidationException.class, () ->
                resourceService.createResource(mockData)
        );
    }

    @Test
    public void testGetResourceDataById() {
        String expectedName = "mockData";
        byte[] expectedData = expectedName.getBytes();
        Resource mockResource = mock(Resource.class);
        when(mockResource.getName()).thenReturn(expectedName);
        when(s3Service.downloadFile(expectedName)).thenReturn(expectedData);
        when(resourceRepository.findById(anyLong())).thenReturn(Optional.of(mockResource));

        byte[] actualData = resourceService.getResourceDataById(1L);

        verify(resourceRepository, times(1)).findById(anyLong());
        assertArrayEquals(expectedData, actualData);
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