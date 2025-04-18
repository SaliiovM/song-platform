package com.microservices.saliiov.resource.resource_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Storage {
    private Long id;
    private String storageType;
    private String bucket;
    private String path;
}
