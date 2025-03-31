package com.microservices.saliiov.resource.processor.contacts;

import com.microservices.saliiov.resource.processor.ResourceProcessorApplication;
import com.microservices.saliiov.resource.processor.dto.ResourceMessage;
import com.microservices.saliiov.resource.processor.dto.SongMetadata;
import com.microservices.saliiov.resource.processor.facade.ResourceProcessorFacade;
import com.microservices.saliiov.resource.processor.service.AudioService;
import com.microservices.saliiov.resource.processor.service.MessageService;
import com.microservices.saliiov.resource.processor.service.ResourceClientService;
import com.microservices.saliiov.resource.processor.service.SongClientService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties.StubsMode.LOCAL;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = MOCK,
        classes = {ResourceProcessorApplication.class},
        properties = "eureka.client.enabled=false"
)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@AutoConfigureStubRunner(
        stubsMode = LOCAL,
        ids = {
                "com.microservices.saliiov.resource:resource-service",
                "com.microservices.saliiov.song:song-service"
        }
)
@ImportAutoConfiguration(TestChannelBinderConfiguration.class)
public class ResourceProcessorFacadeTestIT {

    @SpyBean
    private ResourceClientService resourceClientService;
    @SpyBean
    private SongClientService songClientService;
    @MockBean
    private MessageService messageService;
    @MockBean
    private AudioService audioService;
    @Autowired
    private StubTrigger stubTrigger;
    @SpyBean
    private ResourceProcessorFacade resourceProcessorFacade;

    @Test
    @SneakyThrows
    public void shouldProcessResource() {
        final String resourceId = "123";
        SongMetadata songMetadata = getSongMetadata();
        when(audioService.getSongMetadata("mockAudioData".getBytes())).thenReturn(songMetadata);

        stubTrigger.trigger("send_resource_created_message");

        verify(resourceProcessorFacade, times(1)).processResource(ResourceMessage.builder().id(resourceId).build());
        verify(resourceClientService, times(1)).getResourceById(resourceId);
        verify(songClientService, times(1)).createSong(songMetadata);
        verify(messageService, never()).sendRollbackMessage(resourceId);
    }

    private SongMetadata getSongMetadata() {
        return SongMetadata.builder()
                .name("Mock song")
                .artist("Mock artist")
                .album("Mock album")
                .length("2:30")
                .resourceId(123L)
                .year("2022")
                .build();
    }

}