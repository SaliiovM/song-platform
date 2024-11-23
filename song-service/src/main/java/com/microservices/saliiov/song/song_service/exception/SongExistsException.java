package com.microservices.saliiov.song.song_service.exception;

public class SongExistsException extends RuntimeException {
    public SongExistsException(String message) {
        super(message);
    }
}
