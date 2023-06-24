package com.camping101.beta.web.domain.reservation.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateReservationPaymentRq {

    int price;
    LocalDate startDate;
    LocalDate endDate;


}
