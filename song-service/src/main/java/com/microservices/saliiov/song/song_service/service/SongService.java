package com.microservices.saliiov.song.song_service.service;

import com.microservices.saliiov.song.song_service.dto.SongDto;

import java.util.List;
import java.util.Optional;

/**
 * Service for working with songs
 */
public interface SongService {

    /**
     * Create song
     * @param dto - song data
     * @return - id of created song
     */
    Long createSong(SongDto dto);

    /**
     * Get song by id
     * @param id - song id
     * @return - song data
     */
    Optional<SongDto> findSongById(Long id);

    /**
     * Delete songs by ids
     * @param ids - ids of songs
     * @return - ids of deleted songs
     */
    List<Long> deleteSongsByIds(String ids);
}
