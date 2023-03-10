package com.camping101.beta.reservation.dto;

import com.camping101.beta.reservation.entity.ReservationStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class ReservationCreateRequest {

    private Long memberId;

    private Long siteId;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int humanCapacity;

    private int payment;



}
