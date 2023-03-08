package com.camping101.beta.reservation.service;

import static com.camping101.beta.camp.exception.ErrorCode.CAMP_NOT_FOUND;
import static com.camping101.beta.reservation.entity.ReservationStatus.COMP;
import static com.camping101.beta.reservation.exception.ErrorCode.RESERVATION_NOT_FOUND;
import static com.camping101.beta.site.exception.ErrorCode.SITE_NOT_FOUND;

import com.camping101.beta.camp.entity.Camp;
import com.camping101.beta.camp.exception.CampException;
import com.camping101.beta.camp.repository.CampRepository;
import com.camping101.beta.member.entity.Member;
import com.camping101.beta.member.repository.MemberRepository;
import com.camping101.beta.reservation.dto.ReservationCreateRequest;
import com.camping101.beta.reservation.dto.ReservationCreateResponse;
import com.camping101.beta.reservation.dto.ReservationDetailsResponse;
import com.camping101.beta.reservation.dto.ReservationListResponse;
import com.camping101.beta.reservation.dto.ReservationOwnerListResponse;
import com.camping101.beta.reservation.entity.Reservation;
import com.camping101.beta.reservation.exception.ReservationException;
import com.camping101.beta.reservation.repository.ReservationRepository;
import com.camping101.beta.site.entity.Site;
import com.camping101.beta.site.exception.ErrorCode;
import com.camping101.beta.site.exception.SiteException;
import com.camping101.beta.site.repository.SiteRepository;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
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
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final SiteRepository siteRepository;
    private final ReservationQueryService reservationQueryService;
    private final CampRepository campRepository;


    // 2020 ~ 2024 // 1월 ~ 12월 // 1일 ~ 31일
    static int[][][] dateTime = new int[5][13][32];

    // 사이트 예약 생성
//    1. 예약 가능 일자를 확인하고, 예약 일자를 선택한 후
//    2. 예약 버튼을 누르면 이용 정책을 안내
//    3. 확인 시 결제창으로 이동한다.
//    4. 회원(손님)이 결제를 완료하면 회원(주인)의 카카오톡으로 자동 알림이 전송된다.
    public ReservationCreateResponse registerReservation(
        ReservationCreateRequest reservationCreateRequest) {

        Reservation reservation = Reservation.toEntity(reservationCreateRequest);
        reservationRepository.save(reservation);

        Site findSite = siteRepository.findById(reservationCreateRequest.getSiteId())
            .orElseThrow(() -> {
                throw new SiteException(SITE_NOT_FOUND);
            });

        // 사이트 계산
        reservation.addPayment(findSite.getPrice(), reservationCreateRequest.getStartDate(),
            reservationCreateRequest.getEndDate());

        Member findMember = memberRepository.findById(reservationCreateRequest.getMemberId())
            .orElseThrow(() -> {
                throw new RuntimeException("존재하는 회원이 없습니다");
            });

        // 변경 감지
        reservation.addMember(findMember);
        reservation.addSite(findSite);

        return Reservation.toReservationCreateResponse(reservation);

    }

    //예약 가능 일자 확인
    public void findReservationAbleDate(Long siteId) {

        List<Reservation> reservationList = reservationQueryService.findReservationBySite(siteId);
        for (Reservation reservation : reservationList) {

            LocalDateTime startDate = reservation.getStartDate();
            LocalDateTime endDate = reservation.getEndDate();

            int startYear = startDate.getYear();
            Month startMonth = startDate.getMonth();
            int startDay = startDate.getDayOfMonth();

            int endYear = endDate.getYear();
            Month endMonth = endDate.getMonth();
            int endDay = endDate.getDayOfMonth();

            // 자바 두 날짜사이의 날짜 얻기 블로그 참고

        }

    }

    //======================================================================================================
    //======================================================================================================
    //======================================================================================================
    //======================================================================================================
    //======================================================================================================
    //======================================================================================================


    // 사이트 예약 목록 조회 (회원(손님)기능)
//    1. 회원(손님)은 최대 2년 이내의 예약 내역을 확인할 수 있다.
//    - 필터 : 3개월/6개월/1년/2년
//    - 없으면 “아직 예약을 하지 않았네요~ 예약을 해주세요”
//    2. 예약 사이트의 퇴실일이 지난 경우 캠프 로그 버튼이 활성화된다.
//    - [캠프 로그] 버튼 선택 시, 캠프 로그 등록 페이지로 이동
//    - 예약 목록 조회시 캠프 로그를 쓸수있는지 없는지가 나타남
    // -> 예약 목록 조회시 취소된 예약은 캠프로그를 작성할 수 없게 해야한다.
    public List<ReservationListResponse> findReservationFilterList(Long memberId, int month) {

        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new RuntimeException("존재하는 회원이 없습니다");
        });
        // 예약된 사이트의 퇴실일이 지난 경우의 예약 목록들만 가져오기.
        List<Reservation> reservationList = reservationQueryService.findReservationList(month,
            memberId);

        if (reservationList.size() == 0) {
            log.info("아직 예약을 하지 않았네요~ 예약을 해주세요.");
        }

        // 2. 아직 해당 예약에 대한 캠프로그를 작성하지 않았을 때 캠프로그 버튼 활성화
        // 조건 :  해당 예약이 예약한 기간을 지났으며, 캠프로그를 작성하지 않았고, 예약을 취소한적이 없는경우.
        modifyCampLogWritableYn(reservationList);

        return reservationList.stream().map(Reservation::toReservationListResponse)
            .collect(Collectors.toList());
    }


    // 2. 캠프로그 버튼 활성화 => 예약 사이트의 퇴실일이 지난 경우 캠프 로그 버튼이 활성화된다.
    // 하나의 예약에 대해 여러개의 캠프로그는 쓸수없다. (단 하나의 캠프로그만 가능)
    private void modifyCampLogWritableYn(List<Reservation> reservationList) {

        for (Reservation reservation : reservationList) {

            // 해당 예약이 endDate를 지났을 경우
            if (reservation.getEndDate().isBefore(LocalDateTime.now())) {
                // 이미 캠프로그를 작성한적 있거나 예약이 취소된적 있으면 캠프로그를 더이상 작성할 수 없다.
                if (!reservation.isCampLogYn() && reservation.getStatus() == COMP) {
                    reservation.changeCampLogWritableYn(reservation);
                }
            }
        }
    }

    // 캠핑장 예약 목록 조회 (주인(캠핑장 사장) 기능) -> 초기 화면
    // 캠핑장에 해당하는 모든 예약 목록 조회
    public List<ReservationOwnerListResponse> findReservationOwnerList(Long campId) {

        Camp findCamp = campRepository.findById(campId).orElseThrow(() -> {
            throw new CampException(CAMP_NOT_FOUND);
        });

        List<Long> siteIds = findCamp.getSites().stream().map(Site::getSiteId)
            .collect(Collectors.toList());

        List<Reservation> reservations = new ArrayList<>();

        for (Long siteId : siteIds) {

            Reservation findReservation = reservationRepository.findById(siteId).orElseThrow(() -> {
                throw new ReservationException(RESERVATION_NOT_FOUND);
            });

            reservations.add(findReservation);

        }

        return reservations.stream()
            .map(ReservationOwnerListResponse::new).collect(
                Collectors.toList());
    }

    // 캠핑장 예약 목록 조회 (주인(캠핑장 사장) 기능) -> 검색 필터를 사용한 화면
    // 캠핑장에 해당 사이트의 모든 예약 목록 조회
    @Transactional(readOnly = true)
    public List<ReservationOwnerListResponse> findReservationFilterOwnerList(Long siteId) {

        Site findSite = siteRepository.findById(siteId).orElseThrow(() -> {
            throw new SiteException(SITE_NOT_FOUND);
        });

        List<Reservation> reservations = reservationRepository.findBySite(findSite);

        return reservations.stream()
            .map(ReservationOwnerListResponse::new).collect(
                Collectors.toList());
    }

    // 사이트 예약 상세 조회 (회원(손님) + 주인(캠핑장 사장) 기능)
//   1. 회원(손님)은 마이페이제의 예약 목록 조회후 해당 예약 이름을 누르면 예약의 상세 정보를 볼 수 있다.
    @Transactional(readOnly = true)
    public ReservationDetailsResponse findReservationDetails(Long reservationId) {

        Reservation findReservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> {
                throw new ReservationException(RESERVATION_NOT_FOUND);
            });

        return Reservation.toReservationDetailsResponse(findReservation);
    }

    // 사이트 예약 취소
//    1. 회원(손님)이 결제를 취소하면 회원(주인)의 카카오톡으로 자동 알림이 전송된다.
//    2. 예약 취소의 경우 일주일 전에만 가능하다.
    public void deleteReservation(Long reservationId) {

        Reservation findReservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> {
                throw new ReservationException(RESERVATION_NOT_FOUND);
            });

        if ((findReservation.getStartDate().plusDays(7)).isBefore(LocalDateTime.now())) {
            Reservation.modifyReservationStatus(findReservation);
        } else {
            log.info("예약을 취소할 수 없습니다.");
        }
    }


}
