package com.camping101.beta.member.service;

import com.camping101.beta.member.dto.MemberSignInRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberSignInService extends UserDetailsService {

    void authenticateRequest(MemberSignInRequest request);

    UserDetails loadUserByUsername(String username);

}
