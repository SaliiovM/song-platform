package com.microservices.saliiov.resource.processor.service;

import com.microservices.saliiov.resource.processor.dto.SongMetadata;

/**
 * Service for communicating with the song service
 */
public interface SongClientService {
    /**
     * Create a song in the song service
     * @param songMetadata - song metadata
     * @return - id of created song
     */
    Long createSong(SongMetadata songMetadata);
}
