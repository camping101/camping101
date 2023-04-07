package com.camping101.beta.web.domain.reservation.dto;

import com.camping101.beta.db.entity.reservation.Reservation;
import java.time.LocalDateTime;

import com.camping101.beta.db.entity.reservation.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReservationOwnerListResponse {

    private Long memberId;
    private Long reservationId;
    private Long siteId;
    private String siteName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int humanCapacity;
    private String reservationsStatus;
//    COMP, CANCEL
    private int payment;
    private LocalDateTime createdAt; // 예약일
    private LocalDateTime cancelAt; // 취소일

    public ReservationOwnerListResponse(Reservation reservation) {

        if (reservation.getStatus() == ReservationStatus.COMP) {
            this.memberId = reservation.getMember().getMemberId();
            this.reservationId = reservation.getReservationId();
            this.siteId = reservation.getSite().getSiteId();
            this.siteName = reservation.getSite().getName();
            this.startDate = reservation.getStartDate();
            this.endDate = reservation.getEndDate();
            this.humanCapacity = reservation.getHumanCapacity();
            this.reservationsStatus = String.valueOf(reservation.getStatus());
            this.payment = reservation.getPayment();
            this.createdAt = reservation.getCreatedAt();

        } else {

            this.memberId = reservation.getMember().getMemberId();
            this.reservationId = reservation.getReservationId();
            this.siteId = reservation.getSite().getSiteId();
            this.siteName = reservation.getSite().getName();
            this.startDate = reservation.getStartDate();
            this.endDate = reservation.getEndDate();
            this.humanCapacity = reservation.getHumanCapacity();
            this.reservationsStatus = String.valueOf(reservation.getStatus());
            this.payment = reservation.getPayment();
            this.cancelAt = reservation.getCancelAt();

        }


    }


}
