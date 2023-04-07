package com.camping101.beta.web.domain.camp.controller;

import com.camping101.beta.web.domain.camp.dto.CampCreateRequest;
import com.camping101.beta.web.domain.camp.dto.CampCreateResponse;
import com.camping101.beta.web.domain.camp.dto.CampDetailsAdminResponse;
import com.camping101.beta.web.domain.camp.dto.CampDetailsOwnerResponse;
import com.camping101.beta.web.domain.camp.dto.CampListResponse;
import com.camping101.beta.web.domain.camp.dto.CampModifyRequest;
import com.camping101.beta.web.domain.camp.dto.CampModifyResponse;
import com.camping101.beta.web.domain.camp.dto.campdetaildto.CampDetailsResponse;
import com.camping101.beta.web.domain.camp.service.CampService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/camp")
public class CampController {

    private final CampService campService;

    // 캠핑장 서비스 이용 요청 + 관리자에게 캠핑장 승인 요청
    @PostMapping
    public ResponseEntity<CampCreateResponse> campAdd(
        @RequestBody CampCreateRequest campCreateRequest) {

        CampCreateResponse response = campService.registerCamp(campCreateRequest);

        return ResponseEntity.ok(response);
    }

    // 자신의 캠핑장 목록 조회(주인)
    @GetMapping("/owner/{memberId}")
    public ResponseEntity<Page<CampListResponse>> ownerCampList(
        @PathVariable Long memberId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<CampListResponse> response = campService.findOwnerCampList(pageRequest, memberId);
        return ResponseEntity.ok(response);
    }

    // 캠핑장 목록 조회(회원, 비회원, 관리자)
    @GetMapping
    public ResponseEntity<Page<CampListResponse>> campList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<CampListResponse> responses = campService.findCampList(pageRequest);
        return ResponseEntity.ok(responses);

    }


    // 캠핑장 상세 정보 조회 - 손님
    @GetMapping("/detail/customer/{campId}")
    public ResponseEntity<CampDetailsResponse> campDetailsCustomer(@PathVariable Long campId,
                                                                   @RequestParam(defaultValue = "0") int sitePage,
                                                                   @RequestParam(defaultValue = "5") int siteSize,
                                                                   @RequestParam(defaultValue = "0") int campLogPage,
                                                                   @RequestParam(defaultValue = "5") int campLogSize
    ) {

        PageRequest sitePageRequest = PageRequest.of(sitePage, siteSize);
        PageRequest campLogPageRequest = PageRequest.of(campLogPage, campLogSize);

        CampDetailsResponse response = campService.findCampDetails(campId, sitePageRequest,
            campLogPageRequest);
        return ResponseEntity.ok(response);
    }

    // 캠핑장 상세 정보 조회 - 캠핑장 주인
    @GetMapping("/detail/owner/{campId}")
    public ResponseEntity<CampDetailsOwnerResponse> campDetailsOwner(@PathVariable Long campId) {

        CampDetailsOwnerResponse response = campService.findCampDetailsOwner(campId);
        return ResponseEntity.ok(response);
    }

    // 관리자가 캠핑장 상세 정보 조회
    @GetMapping("/detail/admin/{campId}")
    public ResponseEntity<CampDetailsAdminResponse> campDetailsAdmin(@PathVariable Long campId) {

        CampDetailsAdminResponse response = campService.findCampDetailsAdmin(campId);
        return ResponseEntity.ok(response);

    }

    // 캠핑장 상세 정보 수정
    @PutMapping
    public ResponseEntity<CampModifyResponse> campModify(@RequestBody CampModifyRequest campModifyRequest) {

        CampModifyResponse response = campService.modifyCamp(campModifyRequest);
        return ResponseEntity.ok(response);
    }

    // 캠핑장 서비스 탈퇴 요청
    @DeleteMapping("/{campId}")
    public ResponseEntity<?> campDelete(@PathVariable Long campId) {

        campService.removeCamp(campId);
        return ResponseEntity.ok("캠핑장 삭제 완료");
    }


}
