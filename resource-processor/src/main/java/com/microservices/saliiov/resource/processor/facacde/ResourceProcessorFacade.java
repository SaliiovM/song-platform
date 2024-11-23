package com.microservices.saliiov.resource.processor.facacde;

import com.microservices.saliiov.resource.processor.dto.SongMetadata;
import com.microservices.saliiov.resource.processor.exception.ResourceAlreadyExistsException;
import com.microservices.saliiov.resource.processor.exception.ResourceProcessorException;
import com.microservices.saliiov.resource.processor.service.AudioService;
import com.microservices.saliiov.resource.processor.service.ResourceClientService;
import com.microservices.saliiov.resource.processor.service.SongClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceProcessorFacade {

    private final ResourceClientService resourceClientService;
    private final SongClientService songClientService;
    private final AudioService audioService;

    public void processResource(String resourceId) {
        log.info("Processing resource with id: {}", resourceId);
        if (StringUtils.isNotBlank(resourceId)) {
            try {
                Optional<byte[]> resources = resourceClientService.getResourceById(resourceId);
                if (resources.isPresent()) {
                    SongMetadata songMetadata = getSongMetadata(resourceId, resources.get());
                    Long songId = songClientService.createSong(songMetadata);
                    log.info("Song created with id: {}", songId);
                }
            } catch (ResourceProcessorException | ResourceAlreadyExistsException e) {
                log.error("Failed to process resource with id: {}", resourceId, e);
                resourceClientService.deleteResourceById(resourceId);
            }
        } else {
            log.info("No resource with id: {}", resourceId);
        }
    }

    private SongMetadata getSongMetadata(String resourceId, byte[] resources) {
        SongMetadata songMetadata;
        try {
            songMetadata = audioService.getSongMetadata(resources);
            songMetadata.setResourceId(Long.parseLong(resourceId));
        } catch (IOException | TikaException | SAXException e) {
            log.error("Failed to process resource with id: {}", resourceId, e);
            throw new ResourceProcessorException("Failed to process resource with id: " + resourceId, e);
        }
        return songMetadata;
    }

}
