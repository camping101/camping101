package com.camping101.beta.web.domain.admin.member.service;


import com.camping101.beta.web.domain.member.dto.signin.SignInByEmailRequest;
import com.camping101.beta.web.domain.member.dto.token.TokenInfo;

public interface AdminMemberSignInService {

    TokenInfo signInByEmail(SignInByEmailRequest request);

    boolean isPasswordMatching(Long memberId, String rawPassword, String encodedPassword);
}
