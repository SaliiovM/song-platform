package com.microservices.saliiov.song.song_service.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseError {
    private String message;
}
