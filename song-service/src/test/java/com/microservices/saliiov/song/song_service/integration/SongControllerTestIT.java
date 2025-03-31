package com.microservices.saliiov.song.song_service.integration;

import com.microservices.saliiov.song.song_service.entity.Song;
import com.microservices.saliiov.song.song_service.repository.SongRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(properties = "eureka.client.enabled=false")
public class SongControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SongRepository songRepository;

    @Test
    public void testCreateSong() throws Exception {
        mockMvc.perform(post("/songs")
                        .content("{ \"name\": \"Test Song\", \"artist\": \"Test Singer\", \"album\": \"Test Album\"," +
                                " \"length\": \"2:59\", \"year\": \"2022\", \"resourceId\": \"123\" }")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<Song> song = songRepository.findByNameAndSongYearAndArtist("Test Song", "2022", "Test Singer");
        assertTrue(song.isPresent());
        assertEquals("Test Song", song.get().getName());
        assertEquals("Test Singer", song.get().getArtist());
        assertEquals("Test Album", song.get().getAlbum());
        assertEquals("2:59", song.get().getLength());
        assertEquals("2022", song.get().getSongYear());
        assertEquals(123L, song.get().getResourceId());
    }

    @Test
    public void testGetSong() throws Exception {
        Song song = createSong();

        mockMvc.perform(get("/songs/" + song.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\":\"Test Song\",\"artist\":\"Test Singer\",\"album\":\"Test Album\",\"length\":\"2:59\",\"resourceId\":1,\"year\":\"2022\"}"));
    }

    @Test
    public void testDeleteSongs() throws Exception {
        Song song = createSong();

        mockMvc.perform(delete("/songs")
                        .param("id",String.valueOf(song.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Optional<Song> noSong = songRepository.findById(song.getId());
        assertTrue(noSong.isEmpty());
    }

    private Song createSong() {
        Song testSong = new Song();
        testSong.setName("Test Song");
        testSong.setArtist("Test Singer");
        testSong.setAlbum("Test Album");
        testSong.setSongYear("2022");
        testSong.setResourceId(1L);
        testSong.setLength("2:59");
        return songRepository.save(testSong);
    }

    @AfterEach
    public void tearDown() {
        songRepository.deleteAll();
    }
}