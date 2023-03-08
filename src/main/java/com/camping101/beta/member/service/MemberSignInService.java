package com.camping101.beta.member.service;

import com.camping101.beta.member.dto.MemberSignInRequest;
import com.camping101.beta.member.dto.TokenInfo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberSignInService extends UserDetailsService {

    void authenticateRequest(MemberSignInRequest request);

    UserDetails loadUserByUsername(String username);

    TokenInfo refreshToken(String serverAccessToken, String serverRefreshToken);
}
