package com.camping101.beta.site.controller;

import com.camping101.beta.site.dto.SiteCreateRequest;
import com.camping101.beta.site.dto.SiteCreateResponse;
import com.camping101.beta.site.dto.SiteListResponse;
import com.camping101.beta.site.dto.SiteModifyRequest;
import com.camping101.beta.site.dto.SiteModifyResponse;
import com.camping101.beta.site.service.SiteService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/site")
@RequiredArgsConstructor
public class SiteController {

    private final SiteService siteService;

    // 사이트 생성
    @PostMapping
    public ResponseEntity<SiteCreateResponse> siteAdd(SiteCreateRequest siteCreateRequest) {

        SiteCreateResponse response = siteService.registerSite(siteCreateRequest);
        return ResponseEntity.ok(response);

    }

    // 사이트 목록 조회
    @GetMapping // 쿼리스트링 쓸지 PathVariable 쓸지 정하자.
    public ResponseEntity<List<SiteListResponse>> siteList(@RequestParam Long campId,
        Pageable pageable) {

        List<SiteListResponse> response = siteService.findSiteList(campId, pageable);
        return ResponseEntity.ok(response);

    }

    // 사이트 상세 조회
    @GetMapping("/{siteId}")
    public void siteDetails(@PathVariable Long siteId) {

        siteService.findSiteDetails(siteId);

    }

    // 사이트 수정
    @PutMapping
    public ResponseEntity<SiteModifyResponse> siteModify(SiteModifyRequest siteModifyRequest) {

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
