package com.microservices.saliiov.song.song_service.contracts;

import com.microservices.saliiov.song.song_service.controller.SongController;
import com.microservices.saliiov.song.song_service.dto.SongDto;
import com.microservices.saliiov.song.song_service.service.SongService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;

@SpringBootTest(properties = "eureka.client.enabled=false")
public abstract class SongBaseTestClass {

    @MockBean
    private SongService songService;

    @Autowired
    private SongController songController;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(songController);
        when(songService.createSong(SongDto.builder()
                .name("Mock song")
                .artist("Mock artist")
                .album("Mock album")
                .length("2:30")
                .resourceId(123L)
                .year("2022")
                .build()))
                .thenReturn(321L);
    }
}
