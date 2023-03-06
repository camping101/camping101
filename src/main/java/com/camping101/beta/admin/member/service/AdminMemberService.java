package com.camping101.beta.admin.member.service;

import com.camping101.beta.admin.member.dto.AdminMemberInfoResponse;
import com.camping101.beta.admin.member.dto.AdminMemberListRequest;
import com.camping101.beta.admin.member.dto.AdminMemberListResponse;
import com.camping101.beta.member.entity.status.MemberStatus;
import com.camping101.beta.member.entity.type.MemberType;
import com.camping101.beta.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AdminMemberService {

    private final MemberRepository memberRepository;

    public AdminMemberListResponse getMemberList(AdminMemberListRequest request) {

        var page = PageRequest.of(request.getPageNumber(), request.getRecordSize(), Sort.Direction.DESC, "created_at");
        var memberList = memberRepository.findMembersByMemberType(MemberType.valueOf(request.getMemberType()), page);

        return AdminMemberListResponse.fromEntity(memberList);
    }

    public AdminMemberInfoResponse getMemberInfo(Long memberId) {

        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        return AdminMemberInfoResponse.fromEntity(member);

    }

    @Transactional
    public AdminMemberInfoResponse updateMemberStatus(Long memberId, String memberStatus) {

        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        member.changeMemberStatus(MemberStatus.valueOf(memberStatus));

        return AdminMemberInfoResponse.fromEntity(member);

    }

}
