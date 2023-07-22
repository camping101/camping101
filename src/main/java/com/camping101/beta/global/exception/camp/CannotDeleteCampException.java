package com.camping101.beta.global.exception.camp;


import com.camping101.beta.global.exception.GeneralException;
import org.springframework.http.HttpStatus;

public class CannotDeleteCampException  extends GeneralException {

    public CannotDeleteCampException() {
        super(HttpStatus.BAD_REQUEST, "캠핑장을 삭제할 수 없습니다.");
    }


}
