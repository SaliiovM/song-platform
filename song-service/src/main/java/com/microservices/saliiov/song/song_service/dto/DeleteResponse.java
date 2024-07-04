package com.microservices.saliiov.song.song_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DeleteResponse {
    private List<Long> ids;
}