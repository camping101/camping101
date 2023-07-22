package com.camping101.beta.global.exception.reservation;

import com.camping101.beta.global.exception.GeneralException;
import org.springframework.http.HttpStatus;

public class AlreadyBeenDeletedReservation extends GeneralException {

    public AlreadyBeenDeletedReservation() {
        super(HttpStatus.BAD_REQUEST, "이미 예약이 삭제됐습니다.");
    }

}
