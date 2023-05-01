package com.camping101.beta.web.domain.camp.controller;

import com.camping101.beta.global.path.ApiPath;
import com.camping101.beta.web.domain.camp.dto.CreateCampRq;
import com.camping101.beta.web.domain.camp.dto.CreateCampRs;
import com.camping101.beta.web.domain.camp.dto.FindCampDetailsAdminRs;
import com.camping101.beta.web.domain.camp.dto.FindCampDetailsOwnerRs;
import com.camping101.beta.web.domain.camp.dto.FindCampListRs;
import com.camping101.beta.web.domain.camp.dto.ModifyCampRq;
import com.camping101.beta.web.domain.camp.dto.ModifyCampRs;
import com.camping101.beta.web.domain.camp.dto.campdetaildto.FindCampDetailsRs;
import com.camping101.beta.web.domain.camp.service.CampService;
import com.camping101.beta.web.domain.camp.service.FindCampService;
import io.swagger.annotations.Api;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "캠핑 101 - 캠핑장 API")
public class CampController {

    private final CampService campService;
    private final FindCampService findCampService;

    // 캠핑장 서비스 이용 요청 + 관리자에게 캠핑장 승인 요청
    @PostMapping(ApiPath.CAMP)
    public ResponseEntity<CreateCampRs> campAdd(@RequestBody CreateCampRq rq) {

        CreateCampRs rs = campService.registerCamp(rq);

        return ResponseEntity.ok(rs);
    }

    // 자신의 캠핑장 목록 조회(주인)
    @GetMapping(ApiPath.CAMP_OWNER_MEMBER_ID)
    public ResponseEntity<Page<FindCampListRs>> ownerCampList(
        @PathVariable("member-id") Long memberId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<FindCampListRs> rs = findCampService.findOwnerCampList(pageRequest, memberId);
        return ResponseEntity.ok(rs);
    }

    // 캠핑장 목록 조회(회원, 비회원, 관리자)
    @GetMapping(ApiPath.CAMP)
    public ResponseEntity<Page<FindCampListRs>> campList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<FindCampListRs> rs = findCampService.findCampList(pageRequest);
        return ResponseEntity.ok(rs);
    }

    // 캠핑장 상세 정보 조회 - 손님
    @GetMapping(ApiPath.CAMP_DETAILS_CUSTOMER_CAMP_ID)
    public ResponseEntity<FindCampDetailsRs> campDetailsCustomer(@PathVariable("camp-id") Long campId,
                                                                   @RequestParam(defaultValue = "0") int sitePage,
                                                                   @RequestParam(defaultValue = "5") int siteSize,
                                                                   @RequestParam(defaultValue = "0") int campLogPage,
                                                                   @RequestParam(defaultValue = "5") int campLogSize
    ) {

        PageRequest sitePageRq = PageRequest.of(sitePage, siteSize);
        PageRequest campLogPageRq = PageRequest.of(campLogPage, campLogSize);

        FindCampDetailsRs rs = findCampService.findCampDetails(campId, sitePageRq,
            campLogPageRq);
        return ResponseEntity.ok(rs);
    }

    // 캠핑장 상세 정보 조회 - 캠핑장 주인
    @GetMapping(ApiPath.CAMP_DETAILS_OWNER_CAMP_ID)
    public ResponseEntity<FindCampDetailsOwnerRs> campDetailsOwner(@PathVariable("camp-id") Long campId) {

        FindCampDetailsOwnerRs rs = findCampService.findCampDetailsOwner(campId);
        return ResponseEntity.ok(rs);
    }

    // 관리자가 캠핑장 상세 정보 조회
    @GetMapping(ApiPath.CAMP_DETAILS_ADMIN_CAMP_ID)
    public ResponseEntity<FindCampDetailsAdminRs> campDetailsAdmin(@PathVariable("camp-id") Long campId) {

        FindCampDetailsAdminRs rs = findCampService.findCampDetailsAdmin(campId);
        return ResponseEntity.ok(rs);
    }

    // 캠핑장 상세 정보 수정
    @PutMapping(ApiPath.CAMP)
    public ResponseEntity<ModifyCampRs> campModify(@RequestBody ModifyCampRq modifyCampRq) {

        ModifyCampRs rs = campService.modifyCamp(modifyCampRq);
        return ResponseEntity.ok(rs);
    }

    // 캠핑장 서비스 탈퇴 요청
    @DeleteMapping(ApiPath.CAMP_ID)
    public ResponseEntity<?> campDelete(@PathVariable("camp-id") Long campId) {

        campService.removeCamp(campId);
        return ResponseEntity.ok("캠핑장 삭제 완료");
    }


}
