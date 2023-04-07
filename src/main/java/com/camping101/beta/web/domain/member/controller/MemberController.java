package com.camping101.beta.web.domain.member.controller;

import com.camping101.beta.web.domain.member.dto.MemberInfoResponse;
import com.camping101.beta.web.domain.member.dto.MemberUpdateRequest;
import com.camping101.beta.web.domain.member.service.MemberService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Api(tags = "마이페이지 API")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/{memberId}/temporal-password")
    public ResponseEntity<Void> temporalPasswordSend(@ApiIgnore Principal principal,
                                                     @PathVariable Long memberId){

        memberService.sendTemporalPassword(principal.getName(), memberId);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Long> memberId(@ApiIgnore Principal principal){

        Long memberId = memberService.getMemberId(principal.getName());

        return ResponseEntity.ok(memberId);

    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberInfoResponse> myMemberInfo(@ApiIgnore Principal principal,
                                                           @PathVariable Long memberId){

        MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(principal.getName(), memberId);

        return ResponseEntity.ok(memberInfoResponse);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<MemberInfoResponse> memberUpdate(@ApiIgnore Principal principal,
                                                           @PathVariable Long memberId,
                                                           MemberUpdateRequest request){

        MemberInfoResponse memberUpdateResponse
                = memberService.updateMember(principal.getName(), memberId, request);

        return ResponseEntity.ok(memberUpdateResponse);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> memberDelete(@ApiIgnore Principal principal,
                                             @PathVariable Long memberId) {

        memberService.deleteMember(principal.getName(), memberId);

        return ResponseEntity.ok().build();
    }

}
