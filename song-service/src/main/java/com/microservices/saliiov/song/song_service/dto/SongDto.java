package com.microservices.saliiov.song.song_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongDto {
    private String name;
    private String artist;
    private String album;
    private String length;
    private Long resourceId;
    private String year;
}
