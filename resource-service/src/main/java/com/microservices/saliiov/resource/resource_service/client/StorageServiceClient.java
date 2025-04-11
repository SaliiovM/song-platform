package com.microservices.saliiov.resource.resource_service.client;

import com.microservices.saliiov.resource.resource_service.dto.Storage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${storage-service-name}")
public interface StorageServiceClient {
    @GetMapping(path = "/storages/type/{type}", consumes = "application/json", produces = "application/json")
    ResponseEntity<Storage> getStorageByType(@PathVariable("type") String type);
}
