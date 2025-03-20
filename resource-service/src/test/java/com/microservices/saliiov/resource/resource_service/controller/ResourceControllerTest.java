package com.microservices.saliiov.resource.resource_service.controller;

import com.microservices.saliiov.resource.resource_service.facade.ResourceFacade;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResourceController.class)
public class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResourceFacade resourceFacade;

    @Test
    public void testCreateResources() throws Exception {
        final Long mockId = 1L;
        Mockito.when(resourceFacade.createResource(any(byte[].class))).thenReturn(mockId);
        MockMultipartFile file = new MockMultipartFile("audioData", "orig", "audio/mpeg", "mockaudio".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/resources")
                        .content(file.getBytes())
                        .contentType("audio/mpeg"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":" + mockId + "}"));

    }

    @Test
    public void testGetResources() throws Exception {
        byte[] mockData = "mockAudioData".getBytes();
        Mockito.when(resourceFacade.getResourceDataById(1L)).thenReturn(mockData);

        mockMvc.perform(get("/resources/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteResource() throws Exception {
        Mockito.when(resourceFacade.deleteResourcesByIds("1"))
                .thenReturn(Collections.singletonList(1L));

        mockMvc.perform(delete("/resources").param("id", "1"))
                .andExpect(status().isOk());
    }
}