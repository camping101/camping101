package com.camping101.beta.web.domain.member.service.singup;

import com.camping101.beta.web.domain.member.dto.signup.SignUpByEmailRequest;
import com.camping101.beta.db.entity.member.Member;

public interface MemberSignUpService {

    void signUpByEmail(SignUpByEmailRequest request);
    void signUpByMember(Member member);
    void activateMemberByMailAuthCode(String email, String requestedCode);

}
