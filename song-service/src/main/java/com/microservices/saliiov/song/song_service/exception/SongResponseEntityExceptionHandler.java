package com.microservices.saliiov.song.song_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class SongResponseEntityExceptionHandler {

    @ExceptionHandler(value = {SongValidationException.class})
    public ResponseEntity<ResponseError> handleSongValidationException(SongValidationException e) {
        log.error("SongValidationException", e);
        return ResponseEntity.badRequest()
                .body(ResponseError.builder()
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(value = {SongExistsException.class})
    public ResponseEntity<ResponseError> handleSongExistsException(SongExistsException e) {
        log.error("SongValidationException", e);
        return ResponseEntity.status(409)
                .body(ResponseError.builder()
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ResponseError> handleException(Exception e) {
        log.error("Exception", e);
        return ResponseEntity.internalServerError()
                .body(ResponseError.builder()
                        .message(e.getMessage())
                        .build());
    }
}
