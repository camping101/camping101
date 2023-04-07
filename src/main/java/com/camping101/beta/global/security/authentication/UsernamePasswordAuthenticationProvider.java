package com.camping101.beta.global.security.authentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationServerProxy authenticationServerProxy;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String email = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());
        List<GrantedAuthority> roles = authentication.getAuthorities().stream().collect(Collectors.toList());

        boolean isAuthenticated = authenticationServerProxy.sendSignInByMailRequest(email, password, roles);

        if (!isAuthenticated) {
            log.warn("UsernamePasswordAuthenticationProvider.authenticate : Authentication failed");
            throw new AuthenticationServiceException("Authentication Failed for Member Signed By Email");
        }

        return new UsernamePasswordAuthentication(email, password, roles);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthentication.class.isAssignableFrom(authentication);
    }
}
