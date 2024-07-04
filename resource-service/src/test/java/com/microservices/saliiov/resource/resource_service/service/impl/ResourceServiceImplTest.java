package com.microservices.saliiov.resource.resource_service.service.impl;

import com.microservices.saliiov.resource.resource_service.dto.SongMetadata;
import com.microservices.saliiov.resource.resource_service.entity.Resource;
import com.microservices.saliiov.resource.resource_service.exception.ResourceValidationException;
import com.microservices.saliiov.resource.resource_service.repository.ResourceRepository;
import com.microservices.saliiov.resource.resource_service.service.AudioService;
import lombok.SneakyThrows;
import org.apache.tika.exception.TikaException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
public class ResourceServiceImplTest {

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private ResourceRepository resourceRepository;

    @MockBean
    private AudioService audioService;

    @Autowired
    private ResourceServiceImpl resourceService;

    @Test
    @SneakyThrows
    public void testCreateResource() {
        byte[] mockData = "mockData".getBytes();
        SongMetadata songMetadata = new SongMetadata();
        Long expectedId = 1L;
        Resource mockResource = mock(Resource.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SongMetadata> request = new HttpEntity<>(songMetadata, headers);
        when(mockResource.getId()).thenReturn(expectedId);
        when(resourceRepository.save(any(Resource.class))).thenReturn(mockResource);
        when(audioService.getSongMetadata(mockData)).thenReturn(songMetadata);

        Long resourceId = resourceService.createResource(mockData);

        verify(resourceRepository, times(1)).save(any(Resource.class));
        verify(audioService, times(1)).getSongMetadata(mockData);
        verify(restTemplate, times(1)).postForEntity(anyString(), eq(request), eq(String.class));
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
        when(audioService.getSongMetadata(mockData)).thenThrow(new TikaException("Error"));

        assertThrows(ResourceValidationException.class, () ->
                resourceService.createResource(mockData)
        );
    }

    @Test
    public void testGetResourceDataById() {
        byte[] expectedData = "mockData".getBytes();
        Resource mockResource = mock(Resource.class);
        when(mockResource.getData()).thenReturn(expectedData);
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