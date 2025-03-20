package com.microservices.saliiov.resource.processor.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${resource-service-name}")
public interface ResourceServiceClient {
    @GetMapping("/resources/{id}")
    ResponseEntity<ByteArrayResource> getResourceById(@PathVariable("id") Long id);
}
