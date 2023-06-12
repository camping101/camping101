package com.camping101.beta.web.domain.site.controller;

import com.camping101.beta.global.path.ApiPath;
import com.camping101.beta.web.domain.site.dto.CreateSiteRq;
import com.camping101.beta.web.domain.site.dto.CreateSiteRs;
import com.camping101.beta.web.domain.site.dto.ModifySiteRq;
import com.camping101.beta.web.domain.site.dto.ModifySiteRs;
import com.camping101.beta.web.domain.site.dto.FindSiteListByCampIdRs;
import com.camping101.beta.web.domain.site.dto.sitedetailsresponse.FindSiteDetailsRs;
import com.camping101.beta.web.domain.site.service.FindSiteService;
import com.camping101.beta.web.domain.site.service.SiteService;
import io.swagger.annotations.Api;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "캠핑 101 - 사이트 API")
public class SiteController {

    private final SiteService siteService;
    private final FindSiteService findSiteService;

    // 사이트 생성 => 최초 생성시 웹사이트에 공개하지 않음
    @PostMapping(ApiPath.SITE)
    public CreateSiteRs siteAdd(
        @RequestBody CreateSiteRq createSiteRq) {
        return siteService.registerSite(createSiteRq);
    }


    @GetMapping(ApiPath.SITE_OWNER_CAMP_ID)
    public List<FindSiteListByCampIdRs> siteList(@PathVariable("camp-id") Long campId) {

//        PageRequest pageRequest = PageRequest.of(page, size);

        return findSiteService.findSiteList(campId);

    }

    // 사이트 상세 조회
    @GetMapping(ApiPath.SITE_ID)
    public FindSiteDetailsRs siteDetailsOwner(@PathVariable("site-id") Long siteId) {

        return findSiteService.findSiteDetails(siteId);
    }

    // 사이트 수정
    @PutMapping(ApiPath.SITE)
    public ModifySiteRs siteModify(
        @RequestBody ModifySiteRq modifySiteRq) {

        return siteService.modifySite(modifySiteRq);
    }

    // 사이트 삭제
    @DeleteMapping(ApiPath.SITE_ID)
    public void siteRemove(@PathVariable("site-id") Long siteId) {

        siteService.removeSite(siteId);
    }
}
