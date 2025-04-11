package com.microservices.saliiov.resource.storage.dto;

import lombok.Data;

@Data
public class CreateStorageDto {
    private String storageType;
    private String bucket;
    private String path;
}
