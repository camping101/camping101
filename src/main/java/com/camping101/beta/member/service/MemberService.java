package com.camping101.beta.member.service;

import com.camping101.beta.member.dto.MemberInfoResponse;
import com.camping101.beta.member.dto.MemberPasswordResetResponse;
import com.camping101.beta.member.dto.MemberUpdateRequest;

import java.security.Principal;

public interface MemberService {

    MemberInfoResponse getMemberInfo(String email, Long memberId);
    MemberInfoResponse updateMember(String email, Long memberId, MemberUpdateRequest request);
    MemberPasswordResetResponse sendTemporalPassword(String email);

    void deleteMember(Long memberId, String requesterEmail);

}
