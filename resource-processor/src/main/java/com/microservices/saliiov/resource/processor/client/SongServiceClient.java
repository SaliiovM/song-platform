package com.microservices.saliiov.resource.processor.client;

import com.microservices.saliiov.resource.processor.dto.ResponseId;
import com.microservices.saliiov.resource.processor.dto.SongMetadata;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "${song-service-name}")
public interface SongServiceClient {
    @PostMapping("/songs")
    ResponseEntity<ResponseId> createSong(SongMetadata songMetadata);
}
