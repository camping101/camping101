package com.camping101.beta.site.service;

import static com.camping101.beta.camp.exception.ErrorCode.CAMP_NOT_FOUND;
import static com.camping101.beta.member.entity.type.MemberType.CUSTOMER;
import static com.camping101.beta.site.exception.ErrorCode.SITE_NOT_FOUND;

import com.camping101.beta.camp.entity.Camp;
import com.camping101.beta.camp.exception.CampException;
import com.camping101.beta.camp.repository.CampRepository;
import com.camping101.beta.member.entity.Member;
import com.camping101.beta.member.entity.type.MemberType;
import com.camping101.beta.member.repository.MemberRepository;
import com.camping101.beta.reservation.repository.ReservationRepository;
import com.camping101.beta.reservation.service.ReservationService;
import com.camping101.beta.site.dto.SiteCreateRequest;
import com.camping101.beta.site.dto.SiteCreateResponse;
import com.camping101.beta.site.dto.SiteListResponse;
import com.camping101.beta.site.dto.SiteModifyRequest;
import com.camping101.beta.site.dto.SiteModifyResponse;
import com.camping101.beta.site.dto.sitedetailsresponse.ReservationDto;
import com.camping101.beta.site.dto.sitedetailsresponse.SiteDetailsResponse;
import com.camping101.beta.site.entity.Site;
import com.camping101.beta.site.exception.ErrorCode;
import com.camping101.beta.site.exception.SiteException;
import com.camping101.beta.site.repository.SiteRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class SiteService {

    private final SiteRepository siteRepository;
    private final CampRepository campRepository;
    private final SiteQueryService siteQueryService;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;

    // 사이트 생성
    // 사이트가 실제로 보이려면 openYn을 사이트 수정에서 활성화시켜줘야 한다.
    public SiteCreateResponse registerSite(SiteCreateRequest siteCreateRequest) {

        Site site = Site.toEntity(siteCreateRequest);

        siteRepository.save(site);

        Camp camp = campRepository.findById(siteCreateRequest.getCampId())
            .orElseThrow(() -> {
                throw new SiteException(SITE_NOT_FOUND);
            });

        site.addCamp(camp); // 변경감지로 넣기
        return Site.toSiteCreateResponse(site);

    }

    // 사이트 목록 조회(캠핑장 내의 사이트 조회) + 페이징 처리 하기 => 이거 회원은 필요없고, 주인만 있으면 된다.
    // 캠핑장 사장님 기능이다! => 마이페이지 -> 캠핑장 목록 조회 => (캠핑장목록에서 해당 캠핑장에 사이트 목록 조회 버튼
    // 만들기 => 해당 캠핑장의 사이트 목록 조회
    @Transactional(readOnly = true)
    public Page<SiteListResponse> findSiteList(Long campId, Pageable pageable) {

        Camp camp = campRepository.findById(campId).orElseThrow(() -> {
            throw new CampException(CAMP_NOT_FOUND);
        });

        // 공개 여부가 true 인 값만 목록 조회
        Page<Site> sites = siteRepository.findAllByCampAndOpenYn(camp, true, pageable);

        return sites.map(Site::toSiteListResponse);
    }

    // 사이트 노출 상태 바꾸기 (주인기능) => 주인이 자신의 캠핑장의 사이트들을 목록 조회하면 그 이후
    // 체크박스를 통해 사이트들의 노출상태를 한번에 변경할 수 있음.
    // 만약 해당 사이트에 예약이 아직 존재한다면, 노출 상태를 변경할 수 없다.
    public void changeOpenYn(List<Long> siteIds) {

        List<Long> canChangeOpenYn = new ArrayList<>();

        for (Long siteId : siteIds) {
            isReservationValid(siteId, canChangeOpenYn);
        }

        for (Long validSiteId : canChangeOpenYn) {

            Site findSite = siteRepository.findById(validSiteId).orElseThrow(() -> {
                throw new SiteException(SITE_NOT_FOUND);
            });

            findSite.changeOpenYn(findSite);
        }
    }

    // 여러 사이트의 영업상태(OpenYn)를 수정할때 사용.
    private void isReservationValid(Long siteId, List<Long> canChangeOpenYn) {

        Site findSite = siteRepository.findById(siteId).orElseThrow(() -> {
            throw new SiteException(SITE_NOT_FOUND);
        });

        List<ReservationDto> reservationList = findAllReservationList(siteId);

        for (ReservationDto reservationDto : reservationList) {

            if (reservationDto.getEndDate().isAfter(LocalDateTime.now())) {
                canChangeOpenYn.add(findSite.getSiteId());
            } else {
                // 이부분 어떻게 처리할지 고민하기.(우선 log 를 찍어서 임시방편으로 남겨둠)
                log.info("{} 사이트에 예약이 존재합니다.", findSite.getName());
            }
        }
    }

    //     사이트 상세 조회
//     1. 회원(손님)은 사이트 상세 정보를 확인할 수 있다.
//    - 테이블의 모든 정보
    //- 예약 정보
//    - 캠프 로그
//    - 페이징 처리 (캠프 로그 목록을 페이징)
    @Transactional(readOnly = true)
    public SiteDetailsResponse findSiteDetails(Long siteId, Long memberId) {

        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new RuntimeException("존재하는 회원이 없습니다");
        });

        MemberType memberType = findMember.getMemberType();
        if (memberType == CUSTOMER) {
            return siteQueryService.findSiteDetails(siteId, memberId);
        } else {
            return siteQueryService.findSiteDetails(siteId);
        }
    }

    //     사이트 상세 조회
//     1. 주인(캠핑장 사장)은 사이트 상세 정보를 확인할 수 있다.
//    - 테이블의 모든 정보
//    - 캠프 로그
//    - 페이징 처리 (캠프 로그 목록을 페이징)
    @Transactional(readOnly = true)
    public SiteDetailsResponse findSiteDetails(Long siteId) {

        return siteQueryService.findSiteDetails(siteId);
    }

    // 사이트 수정
    // 만약 해당 사이트에 예약이 아직 존재한다면, 노출 상태를 변경할 수 없다.
    public SiteModifyResponse modifySite(SiteModifyRequest siteModifyRequest) {

        Site findSite = siteRepository.findById(siteModifyRequest.getSiteId())
            .orElseThrow(() -> {
                throw new SiteException(SITE_NOT_FOUND);
            });

        boolean isValid = isReservationValid(findSite.getSiteId());

        if (isValid) {
            Site modifiedSite = findSite.updateSite(siteModifyRequest); // 변경 감지
            return Site.toSiteModifyResponse(modifiedSite);
        } else {
            log.info("{} 사이트에 예약이 존재해 영엽상태를 변경할 수 없습니다",
                findSite.getName());
            return Site.toSiteModifyResponse(findSite);
        }
    }

    // 하나의 사이트만 수정할때 사용
    public boolean isReservationValid(Long siteId) {

        Site findSite = siteRepository.findById(siteId).orElseThrow(() -> {
            throw new SiteException(SITE_NOT_FOUND);
        });

        List<ReservationDto> reservationList = findAllReservationList(siteId);

        for (ReservationDto reservationDto : reservationList) {

            if (!reservationDto.getEndDate().isAfter(LocalDateTime.now())) {
                // 이부분 어떻게 처리할지 고민하기.(우선 log 를 찍어서 임시방편으로 남겨둠)
                log.info("{} 사이트에 예약이 존재합니다.", findSite.getName());
                return false;
            }
        }
        return true;
    }

    // 사이트 관점에서의 예약 목록 조회 (해당 사이트의 모든 예약 목록들을 가져온다.)
    private List<ReservationDto> findAllReservationList(Long siteId) {

        Site findSite = siteRepository.findById(siteId).orElseThrow(() -> {
            throw new SiteException(SITE_NOT_FOUND);
        });

        return reservationRepository.findBySite(findSite)
            .stream().map(ReservationDto::new).collect(Collectors.toList());
    }

    // 사이트 삭제
    public void removeSite(Long siteId) {

        boolean isValid = isReservationValid(siteId);

        if (isValid) {
            siteRepository.deleteById(siteId);
        } else {
            log.info("siteId : {} 사이트에 예약이 존재합니다.", siteId);
        }

    }


}
