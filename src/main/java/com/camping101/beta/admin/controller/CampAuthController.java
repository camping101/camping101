package com.camping101.beta.admin.controller;

import com.camping101.beta.admin.dto.CampAuthAddResponse;
import com.camping101.beta.admin.dto.CampAuthListResponse;
import com.camping101.beta.admin.dto.CampAuthSearchRequest;
import com.camping101.beta.admin.service.CampAuthService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CampAuthController {

    private final CampAuthService campAuthService;

    // api 명세에 추가할것
    // 관리자가 캠핑장 목록 가져오기
    public ResponseEntity<List<CampAuthListResponse>> campAuthList(Pageable pageable) {

        List<CampAuthListResponse> response = campAuthService.findCampAuthList(pageable);
        return ResponseEntity.ok(response);

    }

    // 관리자페이지 - 캠핑장 목록 조회
    public void campAuthList(CampAuthSearchRequest campAuthSearchRequest,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "newest") String order) {

        Sort sort;

        if (order.equals("newest")) {
            sort = Sort.by("createdAt").descending();
        } else {
            sort = Sort.by("createdAt").ascending();
        }

        PageRequest pageRequest = PageRequest.of(page, size, sort);
    }


    // 캠핑장 승인하기(캠핑장 상세보기에서 (내부에서) 하나씩 승인)
    public ResponseEntity<CampAuthAddResponse> campAuthAdd(Long campAuthId) {

        CampAuthAddResponse response = campAuthService.permitCampAuth(campAuthId);

        return ResponseEntity.ok(response);
    }

    // 체크박스를 통해 캠핑장 한번에 승인하기
    public ResponseEntity<List<CampAuthAddResponse>> campAuthListAdd(List<Long> campAuthIds) {

        List<CampAuthAddResponse> responses = campAuthService.permitCampAuthList(campAuthIds);

        return ResponseEntity.ok(responses);

    }


}
