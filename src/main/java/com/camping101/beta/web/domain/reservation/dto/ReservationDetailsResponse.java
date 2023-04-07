package com.camping101.beta.web.domain.reservation.dto;

import com.camping101.beta.db.entity.reservation.ReservationStatus;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReservationDetailsResponse {

    private Long reservationId;

    private Long memberId;

    private Long siteId;
    private String siteName;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int humanCapacity;

    private ReservationStatus status;

    private int payment;

    private LocalDateTime createdAt;

    private LocalDateTime cancelAt;

    private boolean campLogYn; // 예약페이지에서 캠프로그를 작성할지 말지도 결정할 수 있기에 일단 캠프로그 썻는지와

    private boolean campLogWritableYn; // 캠프로그 작성권한이 있는지 넘겨준다.
}
