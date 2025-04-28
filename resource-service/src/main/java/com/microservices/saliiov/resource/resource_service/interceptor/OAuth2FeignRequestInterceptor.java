package com.microservices.saliiov.resource.resource_service.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OAuth2FeignRequestInterceptor implements RequestInterceptor {
    private final OAuth2AuthorizedClientManager authorizedClientManager;
    @Override
    public void apply(RequestTemplate requestTemplate) {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("storage-service")
                .principal("resource-service")
                .build();

        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);

        if (Objects.nonNull(authorizedClient) && Objects.nonNull(authorizedClient.getAccessToken())) {
            String accessToken = authorizedClient.getAccessToken().getTokenValue();
            requestTemplate.header("Authorization", "Bearer " + accessToken);
        }
    }
}
