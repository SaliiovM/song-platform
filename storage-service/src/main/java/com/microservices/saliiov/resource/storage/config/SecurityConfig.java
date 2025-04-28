package com.microservices.saliiov.resource.storage.config;

import com.microservices.saliiov.resource.storage.auth.CombinedAuthManager;
import jakarta.ws.rs.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CombinedAuthManager userAndAdminAuthManager;
    private final CombinedAuthManager adminAuthManager;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/storages/**").access(adminAuthManager)
                        .requestMatchers(HttpMethod.DELETE, "/storages/**").access(adminAuthManager)
                        .requestMatchers(HttpMethod.GET, "/storages/**").access(userAndAdminAuthManager)
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter delegate = new JwtAuthenticationConverter();
        delegate.setJwtGrantedAuthoritiesConverter(jwt -> {

            Collection<String> roles = Optional.ofNullable(jwt.getClaimAsStringList("roles")).orElse(List.of());
            Collection<String> scopes = Optional.ofNullable(jwt.getClaimAsStringList("scope")).orElse(List.of());

            return Stream.concat(roles.stream()
                                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role)),
                            scopes.stream()
                                    .map(scope -> new SimpleGrantedAuthority("SCOPE_" + scope)))
                    .collect(Collectors.toList());
        });
        return delegate;
    }
}
