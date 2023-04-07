package com.camping101.beta.web.domain.member.service;

import com.camping101.beta.web.domain.member.dto.MemberSignInRequest;
import com.camping101.beta.web.domain.member.dto.TokenInfo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberSignInService extends UserDetailsService {

    void authenticateEmailMember(MemberSignInRequest request);

    UserDetails loadUserByUsername(String username);

    TokenInfo refreshToken(String serverRefreshToken);
}
