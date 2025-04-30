package com.microservices.saliiov.resource.storage.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CombinedAuthManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final Set<String> roles;
    private final Set<String> authorities;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext object) {
        Authentication authentication = authenticationSupplier.get();

        if (authentication == null || !authentication.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        Set<String> authRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith("ROLE_"))
                .collect(Collectors.toSet());

        Set<String> authAuthorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith("SCOPE_"))
                .collect(Collectors.toSet());

        boolean hasRole = !CollectionUtils.isEmpty(roles) && authRoles.stream().anyMatch(roles::contains) ;
        boolean hasAuthority = !CollectionUtils.isEmpty(authorities) && authAuthorities.stream().anyMatch(authorities::contains);
        return new AuthorizationDecision(hasRole || hasAuthority);
    }
}
