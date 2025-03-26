package com.microservices.saliiov.resource.resource_service.contracts;

import com.microservices.saliiov.resource.resource_service.controller.ResourceController;
import com.microservices.saliiov.resource.resource_service.facade.ResourceFacade;
import com.microservices.saliiov.resource.resource_service.service.MessageService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;

import static org.mockito.Mockito.when;

@SpringBootTest(properties = "eureka.client.enabled=false")
@AutoConfigureMessageVerifier
@ImportAutoConfiguration(TestChannelBinderConfiguration.class)
public abstract class ResourceBaseTestClass {

    private static final long TEST_ID = 123L;
    @MockBean
    private ResourceFacade resourceFacade;

    @Autowired
    private ResourceController resourceController;

    @Autowired
    private MessageService messageService;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(resourceController);
        when(resourceFacade.getResourceDataById(TEST_ID)).thenReturn("mockAudioData".getBytes());
    }

    public void sendResourceCreatedMessage() {
        messageService.sendResourceCreatedMessage(TEST_ID);
    }
}
