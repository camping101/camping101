package com.camping101.beta.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    MEMBER_SIGN_UP_ERROR("회원가입이 정상적으로 이루어지지 않았습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MEMBER_MAIL_AUTH_RENEW("메일 인증을 재전송 하였습니다. 다시 시도해주세요.",HttpStatus.BAD_REQUEST),
    MEMBER_MAIL_AUTH_FAIL("메일 인증에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MEMBER_SIGN_IN_ERROR("로그인이 정상적으로 이루어지지 않았습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MEMBER_RESET_PASSWORD_FAIL("비밀번호 재설정이 정상적으로 이루어지지 않았습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MEMBER_NOT_MATH("본인 이메일이 맞는지 확인해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;

}
