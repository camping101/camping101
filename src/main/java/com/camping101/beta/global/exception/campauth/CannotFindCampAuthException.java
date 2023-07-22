package com.camping101.beta.global.exception.campauth;

import com.camping101.beta.global.exception.GeneralException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CannotFindCampAuthException extends GeneralException {

    public CannotFindCampAuthException() {
        super(HttpStatus.BAD_REQUEST, "캠프승인을 찾을 수 없습니다.");
    }

}
