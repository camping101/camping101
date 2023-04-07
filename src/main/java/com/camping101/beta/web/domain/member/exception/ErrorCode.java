package com.camping101.beta.web.domain.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    MEMBER_SIGN_UP_ERROR("회원가입이 정상적으로 이루어지지 않았습니다.", HttpStatus.BAD_REQUEST),
    MEMBER_SIGN_IN_ERROR("로그인이 정상적으로 이루어지지 않았습니다.", HttpStatus.BAD_REQUEST),
    MEMBER_TYPE_NOT_FOUND("회원 타입이 누락되었습니다.", HttpStatus.BAD_REQUEST),
    MEMBER_UPDATE_ERROR("회원 수정이 불가합니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MAIL_AUTH_EXPIRED("인증 메일 유효시간이 지나 메일을 재전송 하였습니다.", HttpStatus.BAD_REQUEST),
    MAIL_AUTH_INVALID("인증 메일 요청이 잘못되었습니다.", HttpStatus.BAD_REQUEST),
    TEMPORAL_PASSWORD_ISSUE_FAIL("임시 비밀번호 발급에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    TEMPORAL_PASSWORD_EXPIRED("임시 비밀번호 사용 유효 시간이 지났습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MEMBER_IS_NOT_MATCHING("회원 본인이 아닙니다.", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_INVALID("리프래쉬 토큰 요청이 잘못되었습니다.", HttpStatus.BAD_REQUEST),
    ACCESS_TOKEN_INVALID("엑세스 토큰 요청이 잘못되었습니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

}
