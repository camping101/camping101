package com.camping101.beta.camp.service;

import static com.camping101.beta.camp.entity.ManageStatus.AUTHORIZED;
import static com.camping101.beta.camp.entity.ManageStatus.UNAUTHORIZED;
import static com.camping101.beta.camp.exception.ErrorCode.CAMP_NOT_FOUND;

import com.camping101.beta.admin.entity.CampAuth;
import com.camping101.beta.admin.repository.CampAuthRepository;
import com.camping101.beta.camp.dto.CampCreateRequest;
import com.camping101.beta.camp.dto.CampCreateResponse;
import com.camping101.beta.camp.dto.CampDetailsAdminResponse;
import com.camping101.beta.camp.dto.CampDetailsOwnerResponse;
import com.camping101.beta.camp.dto.CampListResponse;
import com.camping101.beta.camp.dto.CampModifyRequest;
import com.camping101.beta.camp.dto.CampModifyResponse;
import com.camping101.beta.camp.dto.campdetaildto.CampDetailsResponse;
import com.camping101.beta.camp.entity.Camp;
import com.camping101.beta.camp.entity.FacilityCnt;
import com.camping101.beta.camp.entity.Location;
import com.camping101.beta.camp.exception.CampException;
import com.camping101.beta.camp.exception.ErrorCode;
import com.camping101.beta.camp.repository.CampRepository;
import com.camping101.beta.member.entity.Member;
import com.camping101.beta.member.repository.MemberRepository;
import com.camping101.beta.site.entity.Site;
import com.camping101.beta.site.service.SiteService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CampService {

    private final CampRepository campRepository;
    private final MemberRepository memberRepository;
    private final CampQueryService campQueryService;
    private final CampAuthRepository campAuthRepository;
    private final SiteService siteService;

    // 캠핑장 서비스 이용 요청
    public CampCreateResponse registerCamp(CampCreateRequest campCreateRequest) {

        Camp camp = Camp.toEntity(campCreateRequest);
        campRepository.save(camp);

        Long memberId = campCreateRequest.getMemberId();
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new RuntimeException("존재하는 회원이 없습니다");
        });

        camp.addMember(findMember);
        requestAuth(camp);

        log.info("{} 캠핑장이 생성되었습니다. 관리자 승인이 완료되어야 캠핑장 서비스를 이용할 수 있습니다."
            , camp.getName());
        return Camp.toCampCreateResponse(camp);

    }

    // 관리자에게 캠핑장 생성 요청하기
    private void requestAuth(Camp camp) {

        CampAuth campAuth = CampAuth.toEntity(camp);
        campAuthRepository.save(campAuth);
    }

    // 캠핑장 목록 조회(페이징 처리 하기) (주인)
    @Transactional(readOnly = true)
    public Page<CampListResponse> findOwnerCampList(Pageable pageable, Long memberId) {

        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new RuntimeException("존재하는 회원이 없습니다.");
        });

        Page<Camp> camps = campRepository.findAllByMemberAndManageStatus(pageable, findMember,
            AUTHORIZED);

        return camps.map(Camp::toCampListResponse);
    }

    // 캠핑장 목록 조회(페이징 처리 하기) (손님 + 비회원)
    @Transactional(readOnly = true)
    public Page<CampListResponse> findCampList(Pageable pageable) {

        Page<Camp> camps = campRepository.findAllByManageStatus(pageable,AUTHORIZED);
        return camps.map(Camp::toCampListResponse);
    }

    // 캠핑장 상세 정보 조회 -> 회원(손님)
    // 해당 캠핑장의 사이트 목록을 같이 가져온다.
    // 해당 사이트의 모든 캠프로그들도 같이 가져온다.
    @Transactional(readOnly = true)
    public CampDetailsResponse findCampDetails(Long campId, Pageable sitePageable,
        Pageable campLogPageable) {

        return campQueryService.findCampAndSiteAndCampLog(campId, sitePageable, campLogPageable);
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
