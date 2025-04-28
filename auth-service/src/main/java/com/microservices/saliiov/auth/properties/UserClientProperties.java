package com.microservices.saliiov.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "user-client")
public class UserClientProperties {
    private String id;
    private String secret;
    private String redirectUri;
    private String scope;
}
