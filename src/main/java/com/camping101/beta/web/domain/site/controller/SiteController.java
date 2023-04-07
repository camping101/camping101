package com.camping101.beta.web.domain.site.controller;

import com.camping101.beta.web.domain.site.dto.SiteCreateRequest;
import com.camping101.beta.web.domain.site.dto.SiteCreateResponse;
import com.camping101.beta.web.domain.site.dto.SiteListResponse;
import com.camping101.beta.web.domain.site.dto.SiteModifyRequest;
import com.camping101.beta.web.domain.site.dto.SiteModifyResponse;
import com.camping101.beta.web.domain.site.dto.sitedetailsresponse.SiteDetailsResponse;
import com.camping101.beta.web.domain.site.service.SiteService;
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
@RequestMapping("/api/site")
@RequiredArgsConstructor
public class SiteController {

    private final SiteService siteService;

    // 사이트 생성 => 최초 생성시 웹사이트에 공개하지 않음
    @PostMapping
    public ResponseEntity<SiteCreateResponse> siteAdd(
        @RequestBody SiteCreateRequest siteCreateRequest) {

        SiteCreateResponse response = siteService.registerSite(siteCreateRequest);
        return ResponseEntity.ok(response);

    }

    // 사이트 목록 조회
    // 대표이미지/ 사이트명 /가격/기본일정/체크인
    // 회원은 사이트 공개여부를 체크할 수 있다.
    // 단,현재일로 부터 예약된 날짜가 하나라도 있는 경우에 공개 여부를 Y/N으로 바꿀 수 없다.
    @GetMapping("/{campId}")
    public ResponseEntity<Page<SiteListResponse>> siteList(@PathVariable Long campId,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<SiteListResponse> response = siteService.findSiteList(campId, pageRequest);
        return ResponseEntity.ok(response);
    }

    // 사이트 상세 조회 (회원(손님)기능)
    @GetMapping("/customer/{siteId}/{memberId}")
    public ResponseEntity<SiteDetailsResponse> siteDetails(@PathVariable Long siteId,
        @PathVariable Long memberId) {

        SiteDetailsResponse responses = siteService.findSiteDetails(siteId, memberId);

        return ResponseEntity.ok(responses);
    }

    // 사이트 상세 조회 (캠핑장 주인 기능)
    @GetMapping("/owner/{siteId}")
    public ResponseEntity<SiteDetailsResponse> siteDetailsOwner(@PathVariable Long siteId) {

        SiteDetailsResponse responses = siteService.findSiteDetails(siteId);

        return ResponseEntity.ok(responses);
    }

    // 사이트 수정
    @PutMapping
    public ResponseEntity<SiteModifyResponse> siteModify(
        @RequestBody SiteModifyRequest siteModifyRequest) {

        SiteModifyResponse response = siteService.modifySite(siteModifyRequest);
        return ResponseEntity.ok(response);

    }

    // 사이트 삭제
    @DeleteMapping("/{siteId}")
    public ResponseEntity<?> siteRemove(@PathVariable Long siteId) {

        siteService.removeSite(siteId);

        return ResponseEntity.ok("사이트 삭제 완료");
    }


}
