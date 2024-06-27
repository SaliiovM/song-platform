package com.microservices.saliiov.song.song_service.controller;

import com.microservices.saliiov.song.song_service.dto.SongDto;
import com.microservices.saliiov.song.song_service.service.SongService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SongController.class)
public class SongControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SongService songService;

    @Test
    public void testCreateSong() throws Exception {
        Mockito.when(songService.createSong(any(SongDto.class))).thenReturn(1L);
        String songJson = "{ \"songName\": \"Test Song\", \"singerName\": \"Test Singer\", \"albumName\": \"Test Album\" }";

        mockMvc.perform(post("/songs")
                        .content(songJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(songService, Mockito.times(1)).createSong(any(SongDto.class));
    }

    @Test
    public void testGetSong() throws Exception {
        SongDto songDto = new SongDto();
        Mockito.when(songService.findSongById(anyLong())).thenReturn(Optional.of(songDto));

        mockMvc.perform(get("/songs/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(songService, Mockito.times(1)).findSongById(anyLong());
    }

    @Test
    public void testDeleteSongs() throws Exception {
        Mockito.when(songService.deleteSongsByIds(any(String.class))).thenReturn(Collections.singletonList(1L));

        mockMvc.perform(delete("/songs")
                        .param("id","1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(songService, Mockito.times(1)).deleteSongsByIds(any(String.class));
    }
}