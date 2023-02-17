package com.camping101.beta.camp.controller;

import com.camping101.beta.camp.dto.CampCreateRequest;
import com.camping101.beta.camp.dto.CampCreateResponse;
import com.camping101.beta.camp.dto.CampListResponse;
import com.camping101.beta.camp.dto.CampModifyRequest;
import com.camping101.beta.camp.dto.CampModifyResponse;
import com.camping101.beta.camp.service.CampService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/camp")
public class CampController {

    private final CampService campService;

    // 캠핑장 서비스 이용 요청
    @PostMapping
    public ResponseEntity<CampCreateResponse> campAdd(CampCreateRequest campCreateRequest) {

        CampCreateResponse response = campService.registerCamp(campCreateRequest);

        return ResponseEntity.ok(response);
    }

    // 자신의 캠핑장 목록 조회(주인)
    @GetMapping("/{memberId}")
    public ResponseEntity<List<CampListResponse>> ownerCampList(Pageable pageable,
        @PathVariable Long memberId) {

        List<CampListResponse> response = campService.findOwnerCampList(pageable, memberId);
        return ResponseEntity.ok(response);

    }

    // 캠핑장 목록 조회(회원, 비회원)
    @GetMapping
    public ResponseEntity<List<CampListResponse>> campList(Pageable pageable) {

        List<CampListResponse> response = campService.findCampList(pageable);
        return ResponseEntity.ok(response);

    }

    // 캠핑장 상세 정보 조회
    @GetMapping("/{campId}")
    public void campDetails(@PathVariable Long campId) {

        campService.findCampDetails(campId);
    }

    // 캠핑장 상세 정보 수정
    @PutMapping
    public ResponseEntity<CampModifyResponse> campModify(CampModifyRequest campModifyRequest) {

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
