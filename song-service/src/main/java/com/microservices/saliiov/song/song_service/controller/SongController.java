package com.microservices.saliiov.song.song_service.controller;

import com.microservices.saliiov.song.song_service.dto.DeleteResponse;
import com.microservices.saliiov.song.song_service.dto.ResponseId;
import com.microservices.saliiov.song.song_service.dto.SongDto;
import com.microservices.saliiov.song.song_service.service.SongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @PostMapping
    public ResponseEntity<ResponseId> createSong(@RequestBody SongDto songDto) {
        log.debug("Executing createSong");
        return ResponseEntity.ok(ResponseId.builder().id(songService.createSong(songDto)).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDto> getSong(@PathVariable Long id) {
        log.debug("Executing getSong");
        return songService.findSongById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/resources/{resourceId}")
    public ResponseEntity<SongDto> getSongByResourceId(@PathVariable Long resourceId) {
        log.debug("Executing getSongByResourceId");
        return songService.findSongByResourceId(resourceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping
    public ResponseEntity<DeleteResponse> deleteSongs(@RequestParam String id) {
        log.debug("Executing deleteSongs");
        return ResponseEntity.status(HttpStatus.OK)
                .body(DeleteResponse.builder()
                        .ids(songService.deleteSongsByIds(id))
                        .build());
    }
}
