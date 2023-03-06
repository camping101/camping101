package com.camping101.beta.security.authentication;

import com.amazonaws.util.CollectionUtils;
import com.camping101.beta.member.entity.type.SignUpType;
import com.camping101.beta.member.exception.ErrorCode;
import com.camping101.beta.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationServerProxy authenticationServerProxy;
    

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        var email = authentication.getName();
        var password = String.valueOf(authentication.getCredentials());
        var roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        if (CollectionUtils.isNullOrEmpty(roles)) {
            throw new AuthenticationCredentialsNotFoundException("Crendential not Found");
        }

        var result = authenticationServerProxy.sendSignInByMailRequest(email, password, roles);

        if (!result) {
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);
        }

        return new UsernamePasswordAuthentication(email, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthentication.class.isAssignableFrom(authentication);
    }
}
