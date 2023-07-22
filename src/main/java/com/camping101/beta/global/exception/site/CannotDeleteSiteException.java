package com.camping101.beta.global.exception.site;

import com.camping101.beta.global.exception.GeneralException;
import org.springframework.http.HttpStatus;

public class CannotDeleteSiteException extends GeneralException {

    public CannotDeleteSiteException() {
        super(HttpStatus.BAD_REQUEST, "사이트를 삭제할 수 없습니다.");
    }

}
