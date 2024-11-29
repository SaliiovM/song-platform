package com.microservices.saliiov.resource.processor.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${resource-service-name}")
public interface ResourceServiceClient {
    @GetMapping("/resources/{id}")
    ResponseEntity<ByteArrayResource> getResourceById(@PathVariable("id") Long id);

    @DeleteMapping("/resources")
    void deleteResources(@RequestParam("id") String id);
}
