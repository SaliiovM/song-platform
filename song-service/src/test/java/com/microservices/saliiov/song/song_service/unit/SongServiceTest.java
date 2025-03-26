package com.microservices.saliiov.song.song_service.unit;

import com.microservices.saliiov.song.song_service.dto.SongDto;
import com.microservices.saliiov.song.song_service.entity.Song;
import com.microservices.saliiov.song.song_service.exception.SongExistsException;
import com.microservices.saliiov.song.song_service.exception.SongValidationException;
import com.microservices.saliiov.song.song_service.repository.SongRepository;
import com.microservices.saliiov.song.song_service.service.impl.SongServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SongServiceTest {

    @Mock
    private SongRepository songRepository;

    @Test
    public void testCreateSong_Success() {
        SongDto mockDto = new SongDto();
        mockDto.setName("Test Song");
        mockDto.setArtist("Test Artist");
        mockDto.setYear("2022");
        mockDto.setResourceId(1L);

        Song mockSong = new Song();
        mockSong.setId(1L);

        when(songRepository.save(any())).thenReturn(mockSong);
        when(songRepository.findByNameAndSongYearAndArtist(any(), any(), any())).thenReturn(Optional.empty());

        SongServiceImpl songService = new SongServiceImpl(songRepository);
        Long songId = songService.createSong(mockDto);

        assertEquals(1L, songId);
    }

    @Test
    public void testCreateSong_Fails_SongAlreadyExists() {
        SongDto mockDto = new SongDto();
        mockDto.setName("Test Song");
        mockDto.setArtist("Test Artist");
        mockDto.setYear("2022");
        mockDto.setResourceId(1L);

        Song mockSong = new Song();

        when(songRepository.findByNameAndSongYearAndArtist(any(), any(), any())).thenReturn(Optional.of(mockSong));

        SongServiceImpl songService = new SongServiceImpl(songRepository);

        assertThrows(SongExistsException.class, () -> songService.createSong(mockDto));
    }

    @Test
    public void testFindSongById_Success() {
        Song mockSong = new Song();
        mockSong.setName("Test Song");

        when(songRepository.findById(any())).thenReturn(Optional.of(mockSong));

        SongServiceImpl songService = new SongServiceImpl(songRepository);
        Optional<SongDto> song = songService.findSongById(1L);

        assertTrue(song.isPresent());
        assertEquals("Test Song", song.get().getName());
    }

    @Test
    public void testFindSongById_Fails_IdIsNull() {
        SongServiceImpl songService = new SongServiceImpl(songRepository);

        assertThrows(SongValidationException.class, () -> songService.findSongById(null));
    }

    @Test
    public void testDeleteSongsByIds() {
        String ids = "1,2";

        SongServiceImpl songService = new SongServiceImpl(songRepository);
        songService.deleteSongsByIds(ids);

        verify(songRepository).deleteAllById(any());
    }

    @Test
    public void testDeleteSongsByIds_Fails_IdNotNumbers() {
        String ids = "1,a";

        SongServiceImpl songService = new SongServiceImpl(songRepository);

        assertThrows(SongValidationException.class, () -> songService.deleteSongsByIds(ids));
    }
}