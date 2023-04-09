package com.camping101.beta.web.domain.member.service.singup;

import com.camping101.beta.web.domain.member.dto.signup.MemberSignUpRequest;
import com.camping101.beta.db.entity.member.Member;

public interface MemberSignUpService {

    void signUpByEmail(MemberSignUpRequest request);
    void signUpByMember(Member member);
    void activateMemberByMailAuthCode(String email, String requestedCode);

}
