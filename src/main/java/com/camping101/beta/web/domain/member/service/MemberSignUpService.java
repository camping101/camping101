package com.camping101.beta.web.domain.member.service;

import com.camping101.beta.web.domain.member.dto.MemberSignUpRequest;
import com.camping101.beta.db.entity.member.Member;

public interface MemberSignUpService {

    void signUpByEmail(MemberSignUpRequest request);
    void signUpByMember(Member member);
    void activateMemberByAuthCode(String email, String requestedCode);

}
