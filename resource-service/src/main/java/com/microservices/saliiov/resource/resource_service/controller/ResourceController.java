package com.microservices.saliiov.resource.resource_service.controller;

import com.microservices.saliiov.resource.resource_service.dto.DeleteResponse;
import com.microservices.saliiov.resource.resource_service.dto.ResponseId;
import com.microservices.saliiov.resource.resource_service.facade.ResourceFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceFacade resourceFacade;

    @PostMapping(consumes = "audio/mpeg")
    public ResponseEntity<ResponseId> createResources(@RequestBody byte[] audioData) {
        log.debug("Executing createResources");
        return ResponseEntity.ok(ResponseId.builder().id(resourceFacade.createResource(audioData)).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ByteArrayResource> getResources(@PathVariable Long id) {
        log.debug("Executing getResources");
        byte[] resourceDataById = resourceFacade.getResourceDataById(id);
        return Optional.ofNullable(resourceDataById)
                .map(bytes -> ResponseEntity.ok()
                        .contentType(MediaType.valueOf("audio/mpeg"))
                        .body(new ByteArrayResource(bytes)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<DeleteResponse> deleteResources(@RequestParam String id) {
        log.debug("Executing deleteResources");
        return ResponseEntity.status(HttpStatus.OK)
                .body(DeleteResponse.builder()
                        .ids(resourceFacade.deleteResourcesByIds(id))
                        .build());
    }
}
