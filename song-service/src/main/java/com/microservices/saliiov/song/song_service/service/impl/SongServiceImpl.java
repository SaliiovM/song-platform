package com.microservices.saliiov.song.song_service.service.impl;

import com.microservices.saliiov.song.song_service.dto.SongDto;
import com.microservices.saliiov.song.song_service.entity.Song;
import com.microservices.saliiov.song.song_service.exception.SongValidationException;
import com.microservices.saliiov.song.song_service.repository.SongRepository;
import com.microservices.saliiov.song.song_service.service.SongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    @Override
    public Long createSong(SongDto dto) {
        validate(dto);
        Song newSong = mapToSong(dto);
        log.info("Creating song with name: {}, artist: {}, year: {}", newSong.getName(), newSong.getArtist(), newSong.getSongYear());
        return songRepository.save(newSong).getId();
    }

    @Override
    public Optional<SongDto> findSongById(Long id) {
        if (Objects.isNull(id)) {
            throw new SongValidationException("Id is required");
        }
        log.info("Finding song by id: {}", id);
        return songRepository.findById(id).map(this::mapToSongDto);
    }

    @Override
    public List<Long> deleteSongsByIds(String ids) {
        validate(ids);
        List<Long> idsToDelete = Arrays.stream(StringUtils.split(ids, ",")).map(Long::valueOf).toList();
        songRepository.deleteAllById(idsToDelete);
        return idsToDelete;
    }

    private void validate(String ids) {
        if (StringUtils.length(ids) > 200) {
            throw new SongValidationException("Songs ids are too long");
        }

        if (Arrays.stream(StringUtils.split(ids, ",")).anyMatch(id -> !NumberUtils.isDigits(id))) {
            throw new SongValidationException("Songs ids are required and must be numbers");
        }
    }

    private SongDto mapToSongDto(Song song) {
        return SongDto.builder()
                .name(song.getName())
                .artist(song.getArtist())
                .year(song.getSongYear())
                .album(song.getAlbum())
                .length(song.getLength())
                .resourceId(song.getResourceId())
                .build();
    }

    private Song mapToSong(SongDto dto) {
        Song song = new Song();
        song.setName(dto.getName());
        song.setArtist(dto.getArtist());
        song.setSongYear(dto.getYear());
        song.setResourceId(dto.getResourceId());
        song.setAlbum(dto.getAlbum());
        song.setLength(dto.getLength());
        return song;
    }

    private void validate(SongDto dto) {
        if (Objects.isNull(dto)) {
            throw new SongValidationException("SongDto is required");
        }

        if (StringUtils.isAnyBlank(dto.getName(), dto.getArtist(), dto.getYear())
                || Objects.isNull(dto.getResourceId())) {
            throw new SongValidationException("Required fields are missing. Please provide name, artist, year and resourceId");
        }

        if (songRepository.findByNameAndSongYearAndArtist(dto.getName(), dto.getYear(), dto.getArtist()).isPresent()) {
            throw new SongValidationException("Song already exists");
        }
    }
}
