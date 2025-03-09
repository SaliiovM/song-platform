package com.microservices.saliiov.resource.processor.service;

import com.microservices.saliiov.resource.processor.dto.SongMetadata;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * Service for working with audio files
 */
public interface AudioService {
    /**
     * Get metadata of song
     * @param data - song data
     * @return - song metadata
     */
    SongMetadata getSongMetadata(byte[] data) throws IOException, TikaException, SAXException;
}
