package com.microservices.saliiov.resource.storage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {
    private List<StorageConfig> types;

    @Data
    public static class StorageConfig {
        private String storageType;
        private String bucket;
        private String path;
    }
}