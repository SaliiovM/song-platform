package com.microservices.saliiov.resource.storage.config;

import com.microservices.saliiov.resource.storage.auth.CombinedAuthManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public CombinedAuthManager userAndAdminAuthManager() {
        return new CombinedAuthManager(Set.of("ROLE_USER", "ROLE_ADMIN"), Set.of("SCOPE_storage:read", "SCOPE_storage:write"));
    }

    @Bean
    public CombinedAuthManager adminAuthManager() {
        return new CombinedAuthManager(Set.of("ROLE_ADMIN"), Set.of("SCOPE_storage:write"));
    }
}
