package com.camping101.beta.admin.campAuth.controller;

import com.camping101.beta.admin.campAuth.dto.CampAuthAddResponse;
import com.camping101.beta.admin.campAuth.dto.CampAuthListResponse;
import com.camping101.beta.admin.campAuth.dto.CampAuthSearchRequest;
import com.camping101.beta.admin.campAuth.service.CampAuthService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/admin")
public class CampAuthController {

    private final CampAuthService campAuthService;

    // 관리자페이지 - 캠핑장 목록 조회
    // 관리자가 캠핑장 목록 가져오기

    @PostMapping
    public ResponseEntity<Page<CampAuthListResponse>> campAuthList(@RequestBody CampAuthSearchRequest campAuthSearchRequest,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "newest") String order) {

        Sort sort;

        if (order.equals("newest")) {
            sort = Sort.by("createdAt").descending();
        } else {
            sort = Sort.by("createdAt").ascending();
        }

        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<CampAuthListResponse> response = campAuthService.findCampAuthList(pageRequest);
        return ResponseEntity.ok(response);
    }

    // 캠핑장 승인하기(캠핑장 상세보기에서 (내부에서) 하나씩 승인)
    @GetMapping("/{campAuthId}")
    public ResponseEntity<CampAuthAddResponse> campAuthAdd(@PathVariable Long campAuthId) {

        CampAuthAddResponse response = campAuthService.permitCampAuth(campAuthId);

        return ResponseEntity.ok(response);
    }

    // 체크박스를 통해 캠핑장 한번에 승인하기
    @PutMapping
    public ResponseEntity<List<CampAuthAddResponse>> campAuthListAdd(@RequestParam List<Long> campAuthIds) {

        List<CampAuthAddResponse> responses = campAuthService.permitCampAuthList(campAuthIds);

        return ResponseEntity.ok(responses);

    }


}
