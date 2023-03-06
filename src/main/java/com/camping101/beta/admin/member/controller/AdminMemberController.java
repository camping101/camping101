package com.camping101.beta.admin.member.controller;

import com.camping101.beta.admin.member.dto.AdminMemberInfoResponse;
import com.camping101.beta.admin.member.dto.AdminMemberListRequest;
import com.camping101.beta.admin.member.dto.AdminMemberListResponse;
import com.camping101.beta.admin.member.service.AdminMemberService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/member")
@Api(tags = "관리자 회원 관리 API")
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    @GetMapping
    public ResponseEntity<AdminMemberListResponse> getMemberList(@RequestBody AdminMemberListRequest request){

        var memberList = adminMemberService.getMemberList(request);

        return ResponseEntity.ok(memberList);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<AdminMemberInfoResponse> getMemberInfo(@PathVariable Long memberId){

        var memberInfo = adminMemberService.getMemberInfo(memberId);

        return ResponseEntity.ok(memberInfo);
    }

    @PutMapping
    public ResponseEntity<AdminMemberInfoResponse> updateMemberStatus(@RequestParam Long memberId,
                                                                      @RequestParam String memberStatus){

        var updatedMember = adminMemberService.updateMemberStatus(memberId, memberStatus);

        return ResponseEntity.ok(updatedMember);
    }

}
