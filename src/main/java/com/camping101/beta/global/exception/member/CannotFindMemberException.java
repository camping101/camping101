package com.camping101.beta.global.exception.member;

import com.camping101.beta.global.exception.GeneralException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CannotFindMemberException extends GeneralException {

    public CannotFindMemberException() {
        super(HttpStatus.BAD_REQUEST, "멤버를 찾을 수 없습니다.");
    }

}
