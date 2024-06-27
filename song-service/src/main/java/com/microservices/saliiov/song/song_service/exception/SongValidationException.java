package com.microservices.saliiov.song.song_service.exception;

public class SongValidationException extends RuntimeException {
    public SongValidationException(String message) {
        super(message);
    }
}
