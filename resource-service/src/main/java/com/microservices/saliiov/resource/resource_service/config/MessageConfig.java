package com.microservices.saliiov.resource.resource_service.config;

import com.microservices.saliiov.resource.resource_service.dto.ResourceMessage;
import com.microservices.saliiov.resource.resource_service.facade.ResourceFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MessageConfig {

    @Bean
    public Consumer<ResourceMessage> processRollback(ResourceFacade resourceFacade) {
        return resource -> {
            log.info("Processing rollback for resource: {}", resource);
            resourceFacade.deleteResourcesByIds(resource.getId());
        };
    }

    @Bean
    public Consumer<ResourceMessage> resourceCreationSuccess(ResourceFacade resourceFacade) {
        return resource -> {
            log.info("Processing success for resource: {}", resource);
            resourceFacade.processSuccess(resource.getId());
        };
    }
}
