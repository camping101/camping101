package com.camping101.beta.reservation.dto;

import com.camping101.beta.reservation.entity.ReservationStatus;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationListResponse {

    private Long memberId;
    private Long reservationId;
    private Long siteId;
    private String siteName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int humanCapacity;
    private ReservationStatus status;
    private int payment;
    private LocalDateTime createdAt; // 예약일
    private LocalDateTime cancelAt; // 취소일

    @QueryProjection
    public ReservationListResponse(Long memberId, Long reservationId, Long siteId, String siteName,
        LocalDateTime startDate, LocalDateTime endDate, int humanCapacity, ReservationStatus status,
        int payment, LocalDateTime createdAt, LocalDateTime cancelAt) {
        this.memberId = memberId;
        this.reservationId = reservationId;
        this.siteId = siteId;
        this.siteName = siteName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.humanCapacity = humanCapacity;
        this.status = status;
        this.payment = payment;
        this.createdAt = createdAt;
        this.cancelAt = cancelAt;
    }
}
