package com.microservices.saliiov.resource.processor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongMetadata {
    private String name;
    private String artist;
    private String album;
    private String length;
    private Long resourceId;
    private String year;
}
