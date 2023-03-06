package com.camping101.beta.member.service;

import com.camping101.beta.member.dto.MemberSignUpRequest;
import com.camping101.beta.member.entity.Member;

public interface MemberSignUpService {

    void signUpByEmail(MemberSignUpRequest request);
    void signUpByMember(Member member);
    void activateMemberByAuthCode(String email, String requestedCode);

}
