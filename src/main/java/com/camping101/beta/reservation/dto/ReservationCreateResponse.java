package com.camping101.beta.reservation.dto;

import com.camping101.beta.reservation.entity.ReservationStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReservationCreateResponse {

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

    private boolean campLogYn;

    private boolean campLogWritableYn;


}
