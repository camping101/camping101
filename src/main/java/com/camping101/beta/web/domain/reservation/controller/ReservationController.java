package com.camping101.beta.web.domain.reservation.controller;

import com.camping101.beta.web.domain.reservation.dto.ReservationDetailsResponse;
import com.camping101.beta.web.domain.reservation.dto.ReservationListResponse;
import com.camping101.beta.web.domain.reservation.dto.ReservationOwnerListResponse;
import com.camping101.beta.web.domain.reservation.service.ReservationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    // 사이트 예약 생성 => 에러가 너무 많이 터져서 보류
//    1. 예약 가능 일자를 확인하고, 예약 일자를 선택한 후
//    2. 예약 버튼을 누르면 이용 정책을 안내
//    3. 확인 시 결제창으로 이동한다.
//    4. 회원(손님)이 결제를 완료하면 회원(주인)의 카카오톡으로 자동 알림이 전송된다.
//    @PostMapping
//    public ResponseEntity<ReservationCreateResponse> reservationAdd(@RequestBody
//    ReservationCreateRequest reservationCreateRequest) {
//
//        ReservationCreateResponse response = reservationService.registerReservation(
//            reservationCreateRequest);
//
//        return ResponseEntity.ok(response);
//    }


    // 사이트 예약 목록 조회(회원이 자신의 예약 목록 조회)
    // 필터 기능이 존재하는 회원의 예약 목록 조회
    @GetMapping("/customer")
    public ResponseEntity<List<ReservationListResponse>> reservationFilterList(
        @RequestParam Long memberId, @RequestParam int month) {

        List<ReservationListResponse> reservationList = reservationService.findReservationFilterList(
            memberId, month);

        return ResponseEntity.ok(reservationList);
    }

    // 캠핑장 사장의 자신의 캠핑장의 예약 목록 조회(혹은 자신의 캠핑장 사이트의 예약목록 조회)
    @GetMapping("/owner/{campId}")
    public List<ReservationOwnerListResponse> reservationList(@PathVariable Long campId,
        @RequestParam(required = false) Long siteId) {

        if (StringUtils.isEmpty(siteId)) {
            return reservationService.findReservationOwnerList(campId);
        } else {

            return reservationService.findReservationFilterOwnerList(siteId);
        }
    }

    // 사이트 예약 상세 조회
    @GetMapping("/detail/{reservationId}")
    public ResponseEntity<ReservationDetailsResponse> reservationDetails(
        @PathVariable Long reservationId) {

        ReservationDetailsResponse response = reservationService.findReservationDetails(
            reservationId);

        return ResponseEntity.ok(response);
    }

    // 사이트 예약 취소
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> reservationRemove(@PathVariable Long reservationId) {

        reservationService.deleteReservation(reservationId);

        return ResponseEntity.ok("예약이 삭제됐습니다");

    }

}
