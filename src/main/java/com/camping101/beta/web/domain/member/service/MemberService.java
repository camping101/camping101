package com.camping101.beta.web.domain.member.service;

import com.camping101.beta.web.domain.member.dto.MemberInfoResponse;
import com.camping101.beta.web.domain.member.dto.MemberUpdateRequest;

public interface MemberService {

    void sendTemporalPassword(String signInMemberEmail, Long pathMemberId);
    Long getMemberId(String signInMemberEmail);
    MemberInfoResponse getMemberInfo(String signInMemberEmail, Long pathMemberId);
    MemberInfoResponse updateMember(String email, Long memberId, MemberUpdateRequest request);
    void deleteMember(String signInMemberEmail, Long pathMemberId);

}
