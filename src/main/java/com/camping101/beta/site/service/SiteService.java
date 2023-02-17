package com.camping101.beta.site.service;

import com.camping101.beta.camp.entity.Camp;
import com.camping101.beta.camp.repository.CampRepository;
import com.camping101.beta.site.dto.SiteCreateRequest;
import com.camping101.beta.site.dto.SiteCreateResponse;
import com.camping101.beta.site.dto.SiteListResponse;
import com.camping101.beta.site.dto.SiteModifyRequest;
import com.camping101.beta.site.dto.SiteModifyResponse;
import com.camping101.beta.site.entity.Site;
import com.camping101.beta.site.repository.SiteRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SiteService {

    private final SiteRepository siteRepository;
    private final CampRepository campRepository;

    // 사이트 생성
    public SiteCreateResponse registerSite(SiteCreateRequest siteCreateRequest) {

        Site site = Site.toEntity(siteCreateRequest);
        siteRepository.save(site);
        Camp camp = campRepository.findById(siteCreateRequest.getCampId())
            .orElseThrow(() -> {
                throw new RuntimeException("사이트가 존재하지 않습니다");
            });

        site.addCamp(camp); // 변경감지로 넣기

        return Site.toSiteCreateResponse(site);

        // 관리자에게 요청하기

    }

    // 사이트 목록 조회(캠핑장 내의 사이트 조회) + 페이징 처리 하기
    @Transactional(readOnly = true)
    public List<SiteListResponse> findSiteList(Long campId, Pageable pageable) {

        Camp camp = campRepository.findById(campId).orElseThrow(() -> {
            throw new RuntimeException("캠핑장이 존재하지 않습니다");
        });

        Page<Site> sites = siteRepository.findAllByCamp(camp, pageable);

        return sites.stream().map(Site::toSiteListResponse).collect(Collectors.toList());

    }

    // 사이트 상세 조회
    @Transactional(readOnly = true)
    public Site findSiteDetails(Long siteId) {

        return siteRepository.findById(siteId).orElseThrow(() -> {
            throw new RuntimeException("사이트가 존재하지 않습니다.");
        });

    }

    // 사이트 수정
    public SiteModifyResponse modifySite(SiteModifyRequest siteModifyRequest) {

        Site site = siteRepository.findById(siteModifyRequest.getSiteId())
            .orElseThrow(() -> {
                throw new RuntimeException("존재하지 않는 사이트입니다");
            });

        Site modifiedSite = site.updateSite(siteModifyRequest); // 변경 감지

        return Site.toSiteModifyResponse(modifiedSite);

    }

    // 사이트 삭제
    public void removeSite(Long siteId) {

        siteRepository.deleteById(siteId);

    }




}
