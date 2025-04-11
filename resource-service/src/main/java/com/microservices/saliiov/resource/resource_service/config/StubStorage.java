package com.microservices.saliiov.resource.resource_service.config;

import com.microservices.saliiov.resource.resource_service.dto.Storage;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "storage")
public class StubStorage {
    private Storage stub;
}
