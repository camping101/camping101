package com.camping101.beta.web.domain.admin.campAuth.controller;

import com.camping101.beta.global.path.ApiPath;
import com.camping101.beta.web.domain.admin.campAuth.dto.PermitCampAuthRs;
import com.camping101.beta.web.domain.admin.campAuth.dto.FindCampAuthListRs;
import com.camping101.beta.web.domain.admin.campAuth.service.CampAuthService;
import com.camping101.beta.web.domain.admin.campAuth.service.FindCampAuthService;
import io.swagger.annotations.Api;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "주인사이트 - 캠핑장관리 API")
public class CampAuthController {

    private final CampAuthService campAuthService;
    private final FindCampAuthService findCampAuthService;

    // 관리자페이지 - 캠핑장 목록 조회
    // 관리자가 캠핑장 목록 가져오기
    @PostMapping(ApiPath.ADMIN)
    public ResponseEntity<Page<FindCampAuthListRs>> campAuthList(
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "newest") String order) {

        Sort sort;

        if (order.equals("newest")) {
            sort = Sort.by("createdAt").descending();
        } else {
            sort = Sort.by("createdAt").ascending();
        }

        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<FindCampAuthListRs> rs = findCampAuthService.findCampAuthList(pageRequest);
        return ResponseEntity.ok(rs);
    }

    // 캠핑장 승인하기(캠핑장 상세보기에서 (내부에서) 하나씩 승인)
    @GetMapping(ApiPath.ADMIN_CAMPAUTH_ID)
    public ResponseEntity<PermitCampAuthRs> campAuthAdd(@PathVariable("campAuth-id") Long campAuthId) {

        PermitCampAuthRs rs = campAuthService.permitCampAuth(campAuthId);

        return ResponseEntity.ok(rs);
    }

    // 체크박스를 통해 캠핑장 한번에 승인하기
    @PutMapping(ApiPath.ADMIN)
    public ResponseEntity<List<PermitCampAuthRs>> campAuthListAdd(
        @RequestParam List<Long> campAuthIds) {

        List<PermitCampAuthRs> rs = campAuthService.permitCampAuthList(campAuthIds);

        return ResponseEntity.ok(rs);

    }


}
