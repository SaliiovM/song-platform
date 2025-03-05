package com.microservices.saliiov.resource.resource_service.service.impl;

import com.microservices.saliiov.resource.resource_service.dto.SongMetadata;
import com.microservices.saliiov.resource.resource_service.entity.Resource;
import com.microservices.saliiov.resource.resource_service.exception.ResourceValidationException;
import com.microservices.saliiov.resource.resource_service.service.AudioService;
import com.microservices.saliiov.resource.resource_service.service.ResourceService;
import com.microservices.saliiov.resource.resource_service.repository.ResourceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    @Value("${song.base-url}")
    private String baseUrl;
    @Value("${song.songs-endpoint}")
    private String songsEndpoint;
    private String fullUrl;
    private final RestTemplate restTemplate;
    private final ResourceRepository resourceRepository;
    private final AudioService audioService;

    @PostConstruct
    public void init() {
        fullUrl = baseUrl + songsEndpoint;
    }

    @Override
    @Transactional
    public Long createResource(byte[] data) {
        if (ArrayUtils.isEmpty(data)) {
            throw new ResourceValidationException("Audio data is empty");
        }

        try {
            SongMetadata songMetadata = audioService.getSongMetadata(data);
            Long id = saveResource(data);
            songMetadata.setResourceId(id);
            saveMetadata(songMetadata);
            return id;
        } catch (IOException | TikaException | SAXException | HttpClientErrorException e) {
            log.error("Error creating resource: ", e);
            throw new ResourceValidationException("Error creating resource");
        }
    }

    @Override
    public byte[] getResourceDataById(Long id) {
        return Optional.ofNullable(id).map(resourceRepository::findById)
                .orElseThrow(() -> new ResourceValidationException("Resource id is required"))
                .map(Resource::getData)
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
        Resource resource = new Resource();
        resource.setData(data);
        return resourceRepository.save(resource).getId();
    }

    private void saveMetadata(SongMetadata songMetadata) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SongMetadata> request = new HttpEntity<>(songMetadata, headers);
        restTemplate.postForEntity(fullUrl, request, String.class);
    }
}
