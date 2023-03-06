package com.camping101.beta.reservation.controller;

import com.camping101.beta.reservation.dto.ReservationCreateRequest;
import com.camping101.beta.reservation.dto.ReservationCreateResponse;
import com.camping101.beta.reservation.dto.ReservationDetailsResponse;
import com.camping101.beta.reservation.dto.ReservationListResponse;
import com.camping101.beta.reservation.dto.ReservationOwnerListResponse;
import com.camping101.beta.reservation.service.ReservationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    // 사이트 예약 생성
//    1. 예약 가능 일자를 확인하고, 예약 일자를 선택한 후
//    2. 예약 버튼을 누르면 이용 정책을 안내
//    3. 확인 시 결제창으로 이동한다.
//    4. 회원(손님)이 결제를 완료하면 회원(주인)의 카카오톡으로 자동 알림이 전송된다.
    @PostMapping
    public ResponseEntity<ReservationCreateResponse> reservationAdd(
        ReservationCreateRequest reservationCreateRequest) {

        ReservationCreateResponse response = reservationService.registerReservation(
            reservationCreateRequest);

        return ResponseEntity.ok(response);
    }

//    private List<TimeDto> reservationDaySelect(Long siteId) {
//
//        int[][][] reservation = new int[13][][];
//
//
//
//
//
//    }


    // 사이트 예약 목록 조회(회원이 자신의 예약 목록 조회)
    @GetMapping
    public ResponseEntity<List<ReservationListResponse>> reservationList(
        @RequestParam Long memberId, @RequestParam int month) {

        List<ReservationListResponse> reservationList = reservationService.findReservationList(
            memberId, month);

        return ResponseEntity.ok(reservationList);
    }

    // 사이트 예약 목록 조회(회원이 자신의 예약 목록 조회)
    public List<ReservationOwnerListResponse> reservationOwnerList(@RequestParam Long campId,
        @RequestParam(required = false) Long siteId) {

        if (StringUtils.isEmpty(siteId)) {
            return reservationService.findAllReservationOwnerList(campId);
        } else {

            return reservationService.findReservationOwnerList(siteId);
        }
    }

    // 사이트 예약 상세 조회
    @GetMapping("{reservationId}")
    public ResponseEntity<ReservationDetailsResponse> reservationDetails(
        @PathVariable Long reservationId) {

        ReservationDetailsResponse response = reservationService.findReservationDetails(
            reservationId);

        return ResponseEntity.ok(response);

    }

    // 사이트 예약 취소
    @DeleteMapping("{reservationId}")
    public ResponseEntity<?> reservationRemove(@PathVariable Long reservationId) {

        reservationService.deleteReservation(reservationId);

        return ResponseEntity.ok("예약이 삭제됐습니다");

    }

}
