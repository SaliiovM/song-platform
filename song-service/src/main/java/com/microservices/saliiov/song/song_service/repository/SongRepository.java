package com.microservices.saliiov.song.song_service.repository;

import com.microservices.saliiov.song.song_service.entity.Song;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SongRepository extends CrudRepository<Song, Long> {
    /**
     * Find song by name, year and artist
     * @param name - song name
     * @param songYear - song year
     * @param artist - song artist
     * @return - Optional of Song
     */
    Optional<Song> findByNameAndSongYearAndArtist(String name, String songYear, String artist);

    /**
     * Find song by resourceId
     * @param resourceId - song resourceId
     * @return - Optional of Song
     */
    Optional<Song> findByResourceId(Long resourceId);
}
