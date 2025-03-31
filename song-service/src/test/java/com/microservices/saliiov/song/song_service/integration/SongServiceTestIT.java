package com.microservices.saliiov.song.song_service.integration;

import com.microservices.saliiov.song.song_service.dto.SongDto;
import com.microservices.saliiov.song.song_service.exception.SongExistsException;
import com.microservices.saliiov.song.song_service.exception.SongValidationException;
import com.microservices.saliiov.song.song_service.repository.SongRepository;
import com.microservices.saliiov.song.song_service.service.SongService;
import com.microservices.saliiov.song.song_service.service.impl.SongServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "eureka.client.enabled=false")
public class SongServiceTestIT {

    @Autowired
    private SongRepository songRepository;

    private SongService songService;

    @BeforeEach
    public void setUp() {
        songService = new SongServiceImpl(songRepository);
        songService.createSong(SongDto.builder()
                .name("#1 Initial Test song")
                .artist("#1 Initial Test artist")
                .year("2022")
                .resourceId(1L)
                .build());
        songService.createSong(SongDto.builder()
                .name("#2 Initial Test song")
                .artist("#2 Initial Test artist")
                .year("2022")
                .resourceId(1L)
                .build());
    }

    @Test
    public void testCreateSong_Success() {
        SongDto dto = new SongDto();
        dto.setName("Created Test Song");
        dto.setArtist("Created Test Artist");
        dto.setYear("2077");
        dto.setResourceId(2L);

        Long songId = songService.createSong(dto);

        Optional<SongDto> songById = songService.findSongById(songId);
        assertTrue(songById.isPresent());
        assertEquals(dto.getName(), songById.get().getName());
        assertEquals(dto.getArtist(), songById.get().getArtist());
        assertEquals(dto.getYear(), songById.get().getYear());
    }

    @Test
    public void testCreateSong_Fails_SongAlreadyExists() {
        SongDto dto = new SongDto();
        dto.setName("#2 Initial Test song");
        dto.setArtist("#2 Initial Test artist");
        dto.setYear("2022");
        dto.setResourceId(1L);

        assertThrows(SongExistsException.class, () -> songService.createSong(dto));
    }

    @Test
    public void testFindSongById_Success() {
        Long songId = createSong();

        Optional<SongDto> songById = songService.findSongById(songId);

        assertTrue(songById.isPresent());
        assertEquals("#3 Initial Test song", songById.get().getName());
        assertEquals("#3 Initial Test artist", songById.get().getArtist());
    }

    private Long createSong() {
        return songService.createSong(SongDto.builder()
                .name("#3 Initial Test song")
                .artist("#3 Initial Test artist")
                .year("2022")
                .resourceId(123L)
                .build());
    }

    @Test
    public void testFindSongById_Fails_IdIsNull() {
        assertThrows(SongValidationException.class, () -> songService.findSongById(null));
    }

    @Test
    public void testDeleteSongsByIds() {
        String ids = "1,2";

        songService.deleteSongsByIds(ids);

        assertTrue(songService.findSongById(1L).isEmpty());
        assertTrue(songService.findSongById(2L).isEmpty());
    }

    @Test
    public void testDeleteSongsByIds_Fails_IdNotNumbers() {
        String ids = "1,a";
        assertThrows(SongValidationException.class, () -> songService.deleteSongsByIds(ids));
    }

    @AfterEach
    public void tearDown() {
        songRepository.deleteAll();
    }
}