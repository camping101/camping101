package com.camping101.beta.member.controller;

import com.camping101.beta.member.dto.MemberInfoResponse;
import com.camping101.beta.member.dto.MemberPasswordResetResponse;
import com.camping101.beta.member.dto.MemberUpdateRequest;
import com.camping101.beta.member.service.MemberService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Api(tags = "마이페이지 API")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberInfoResponse> getMemberInfo(@ApiIgnore Principal principal,
                                                            @PathVariable Long memberId){

        var memberInfoResponse = memberService.getMemberInfo(principal.getName(), memberId);

        return ResponseEntity.ok(memberInfoResponse);
    }

    @PostMapping("/password")
    public ResponseEntity<MemberPasswordResetResponse> sendTemporalPassword(
            @RequestBody Map<String, String> emailMap){

        var memberPasswordResetResponse = memberService.sendTemporalPassword(emailMap.get("email"));

        return ResponseEntity.ok(memberPasswordResetResponse);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<MemberInfoResponse> updateMemberInfo(@ApiIgnore Principal principal,
                                                               @PathVariable Long memberId,
                                                               MemberUpdateRequest request){

        var memberUpdateResponse = memberService.updateMember(principal.getName(), memberId, request);

        return ResponseEntity.ok(memberUpdateResponse);
    }

    @DeleteMapping("/{memberId}")
    public void deleteMember(@ApiIgnore Principal principal,
                             @PathVariable Long memberId,
                             @ApiIgnore HttpServletResponse response) throws IOException {

        memberService.getMemberInfo(principal.getName(), memberId);
        memberService.deleteMember(memberId);

        response.sendRedirect("/api/member/logout");
    }

}
