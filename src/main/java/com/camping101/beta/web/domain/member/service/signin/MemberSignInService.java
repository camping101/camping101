package com.camping101.beta.web.domain.member.service.signin;

import com.camping101.beta.global.security.MemberDetails;
import com.camping101.beta.web.domain.member.dto.token.TokenInfo;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberSignInService extends UserDetailsService {

    MemberDetails loadUserByUsername(String username);

    boolean isPasswordMatching(MemberDetails memberDetails, String rawPassword);

    TokenInfo refreshToken(String serverRefreshToken);
}
