package com.microservices.saliiov.resource.processor.stream;

import com.microservices.saliiov.resource.processor.facacde.ResourceProcessorFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceProcessingStream {

    private final ResourceProcessorFacade resourceProcessorFacade;

    @Bean
    public Consumer<Message<String>> process() {
        return message -> {
            String resourceId = message.getPayload();
            log.info("Processing resource with id: {}", resourceId);
            resourceProcessorFacade.processResource(resourceId);
        };
    }
}
