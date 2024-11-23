package com.microservices.saliiov.resource.processor.service.impl;

import com.microservices.saliiov.resource.processor.client.SongServiceClient;
import com.microservices.saliiov.resource.processor.dto.ResponseId;
import com.microservices.saliiov.resource.processor.dto.SongMetadata;
import com.microservices.saliiov.resource.processor.exception.ClientRetryException;
import com.microservices.saliiov.resource.processor.exception.ResourceAlreadyExistsException;
import com.microservices.saliiov.resource.processor.exception.ResourceProcessorException;
import com.microservices.saliiov.resource.processor.service.SongClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongClientServiceImpl implements SongClientService {

    private final SongServiceClient songServiceClient;

    @Retryable(
            retryFor = {ClientRetryException.class},
            backoff = @Backoff(delay = 2000)
    )
    @Override
    public Long createSong(SongMetadata songMetadata) {
        if (Objects.isNull(songMetadata)) {
            throw new ResourceProcessorException("Failed to send a request. Song metadata is empty");
        }

        ResponseEntity<ResponseId> song = songServiceClient.createSong(songMetadata);
        if (Objects.nonNull(song.getBody())) {
            return song.getBody().getId();
        } else {
            log.error("Failed to create song. Song metadata: {}", songMetadata);
            throw new ResourceProcessorException("Failed to create song");
        }
    }

    @Recover
    public Long recover(ResourceAlreadyExistsException e) {
        log.error("Failed to create song. Song already exists", e);
        throw new ResourceProcessorException("Failed to create song");
    }
}