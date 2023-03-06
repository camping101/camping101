package com.camping101.beta.security.authentication;

import com.camping101.beta.member.entity.type.MemberType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class UsernamePasswordAuthentication extends UsernamePasswordAuthenticationToken {

    public UsernamePasswordAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public UsernamePasswordAuthentication(Object principal, Object credentials, String memberType) {
        super(principal, credentials, List.of(MemberType.valueOf(memberType.toUpperCase(Locale.ROOT))));
    }

    public UsernamePasswordAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
