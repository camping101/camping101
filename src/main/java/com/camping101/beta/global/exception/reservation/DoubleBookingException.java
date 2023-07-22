package com.camping101.beta.global.exception.reservation;

import com.camping101.beta.global.exception.GeneralException;
import org.springframework.http.HttpStatus;

public class DoubleBookingException extends GeneralException {

    public DoubleBookingException() {
        super(HttpStatus.BAD_REQUEST, "이미 예약을 진행하셨습니다.");
    }

}
