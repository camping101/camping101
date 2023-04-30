package com.camping101.beta.web.domain.camp.service;

import static com.camping101.beta.web.domain.camp.exception.ErrorCode.CAMP_NOT_FOUND;

import com.camping101.beta.web.domain.admin.campAuth.service.CampAuthService;
import com.camping101.beta.web.domain.camp.dto.CreateCampRq;
import com.camping101.beta.web.domain.camp.dto.CreateCampRs;
import com.camping101.beta.web.domain.camp.dto.CampDetailsAdminResponse;
import com.camping101.beta.web.domain.camp.dto.CampDetailsOwnerResponse;
import com.camping101.beta.web.domain.camp.dto.CampModifyRequest;
import com.camping101.beta.web.domain.camp.dto.CampModifyResponse;
import com.camping101.beta.db.entity.camp.Camp;
import com.camping101.beta.web.domain.camp.exception.CampException;
import com.camping101.beta.web.domain.camp.repository.CampRepository;
import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.site.Site;
import com.camping101.beta.web.domain.member.service.FindMemberService;
import com.camping101.beta.web.domain.site.service.SiteService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CampService {

    private final CampRepository campRepository;
    private final FindMemberService findMemberService;
    private final CampQueryService campQueryService;
    private final CampAuthService campAuthService;
    private final SiteService siteService;

    // 캠핑장 서비스 이용 요청
    public CreateCampRs registerCamp(CreateCampRq rq) {

        Camp camp = CreateCampRq.createCamp(rq);
        campRepository.save(camp);

        Member findMember = findMemberService.findMemberOrElseThrow(rq.getMemberId());

        camp.addMember(findMember);

        campAuthService.requestCampAuth(camp);

        log.info("{} 캠핑장이 생성되었습니다. 관리자 승인이 완료되어야 캠핑장 서비스를 이용할 수 있습니다."
            , camp.getName());

        return CreateCampRs.createCampRs(camp);
    }

    // 캠핑장 상세 정보 조회 -> 주인(캠핑장 사장)
    @Transactional(readOnly = true)
    public CampDetailsOwnerResponse findCampDetailsOwner(Long campId) {

        Camp findCamp = campRepository.findById(campId).orElseThrow(() -> {
            throw new CampException(CAMP_NOT_FOUND);
        });

        return Camp.toCampDetailsOwnerResponse(findCamp);
    }

    // 관리자 페이지 -> 캠핑장 상세 정보 조회 (관리자)
    // 캠핑장 정보들만 제공한다.(사이트정보나 캠프로그 정보 제공 x)
    @Transactional(readOnly = true)
    public CampDetailsAdminResponse findCampDetailsAdmin(Long campId) {

        Camp findCamp = campRepository.findById(campId).orElseThrow(() -> {
            throw new CampException(CAMP_NOT_FOUND);
        });

        return Camp.toCampDetailsAdminResponse(findCamp);
    }

    // 사장의 마이페이지 -> 캠핑장 목록 조회 -> 캠핑장 상세 정보 보기 -> 캠핑장 상세 정보 수정 버튼 누름
    // -> 캠핑장 상세 정보 수정
    public CampModifyResponse modifyCamp(CampModifyRequest campEditRequest) {

        Camp camp = campRepository.findById(campEditRequest.getCampId()).orElseThrow(() -> {
            throw new CampException(CAMP_NOT_FOUND);
        });

        Camp modifiedCamp = camp.updateCamp(campEditRequest);

        return Camp.toCampModifyResponse(modifiedCamp);
    }

    // 캠핑장 서비스 탈퇴 요청
    // 캠핑장 탈퇴요청시 해당 캠핑장에 예약되어있는 사이트가 없어야 한다.
    public void removeCamp(Long campId) {

        Camp findCamp = campRepository.findById(campId).orElseThrow(() -> {
            throw new CampException(CAMP_NOT_FOUND);
        });

        List<Long> siteIds = findCamp.getSites().stream().map(Site::getSiteId)
            .collect(Collectors.toList());

        boolean ableRemove = true;

        for (Long siteId : siteIds) {
            boolean isValid = siteService.isReservationValid(siteId);
            if (!isValid) {
                ableRemove = false;
            }
        }

        if (ableRemove) {
            campRepository.delete(findCamp);
        } else {
            log.info("캠핑장에 예약이 존재하여 삭제할 수 없습니다.");
        }
    }

}
