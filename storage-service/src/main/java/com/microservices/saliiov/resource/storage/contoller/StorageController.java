package com.microservices.saliiov.resource.storage.contoller;

import com.microservices.saliiov.resource.storage.dto.CreateStorageDto;
import com.microservices.saliiov.resource.storage.dto.GetStorageDto;
import com.microservices.saliiov.resource.storage.dto.StorageId;
import com.microservices.saliiov.resource.storage.service.StorageService;
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

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/storages")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @PostMapping
    public ResponseEntity<StorageId> createStorage(@RequestBody CreateStorageDto dto) {
        log.info("Creating storage: {}", dto);
        return ResponseEntity.ok(StorageId.builder().id(storageService.createStorage(dto)).build());
    }

    @GetMapping
    public ResponseEntity<List<GetStorageDto>> getStorages() {
        log.info("Getting storages");
        List<GetStorageDto> storages = storageService.getAllStorages();
        return ResponseEntity.ok(storages);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<GetStorageDto> getStorageByType(@PathVariable("type") String type) {
        log.info("Getting storage by type: {}", type);
        return ResponseEntity.ok(storageService.getStorageByType(type));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteStorages(@RequestParam String id) {
        log.debug("Executing deleteStorages");
        storageService.deleteStorages(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
