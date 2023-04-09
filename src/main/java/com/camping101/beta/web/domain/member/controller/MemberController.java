package com.camping101.beta.web.domain.member.controller;

import com.camping101.beta.global.security.MemberDetails;
import com.camping101.beta.web.domain.member.dto.mypage.MemberInfoResponse;
import com.camping101.beta.web.domain.member.dto.mypage.MemberUpdateRequest;
import com.camping101.beta.web.domain.member.service.mypage.MemberService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Api(tags = "캠핑 101 - 마이페이지 API")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberInfoResponse> myMemberInfo(@ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails){

        MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(memberDetails.getEmail(), memberDetails.getMemberId());

        return ResponseEntity.ok(memberInfoResponse);
    }

    @PutMapping
    public ResponseEntity<MemberInfoResponse> memberUpdate(@ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails,
                                                           MemberUpdateRequest request){

        MemberInfoResponse memberUpdateResponse
                = memberService.updateMember(memberDetails.getEmail(), memberDetails.getMemberId(), request);

        return ResponseEntity.ok(memberUpdateResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> memberDelete(@ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails) {

        memberService.deleteMember(memberDetails.getEmail(), memberDetails.getMemberId());

        return ResponseEntity.ok().build();
    }

}
