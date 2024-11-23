package com.microservices.saliiov.resource.processor.config;

import com.microservices.saliiov.resource.processor.dto.ResourceMessage;
import com.microservices.saliiov.resource.processor.facade.ResourceProcessorFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class MessageConfig {

    @Bean
    public Consumer<ResourceMessage> processResourceMessage(ResourceProcessorFacade resourceProcessorFacade) {
        return resource -> {
            log.info("Processing resource with id: {}", resource);
            resourceProcessorFacade.processResource(resource);
        };
    }
}
