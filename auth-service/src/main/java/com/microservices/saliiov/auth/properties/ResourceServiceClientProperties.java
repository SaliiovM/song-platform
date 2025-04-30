package com.microservices.saliiov.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "resource-client")
public class ResourceServiceClientProperties {
    private String id;
    private String secret;
    private List<String> scopes;
}
