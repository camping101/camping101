package com.camping101.beta.web.domain.campLog.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    CAMPLOG_RECTAG_MAX_LIMIT("추천 태그는 4개까지만 등록 가능합니다.", HttpStatus.BAD_REQUEST),
    CAMPLOG_NOT_FOUND("해당 추천 태그가 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    CAMPLOG_WRITER_MISMATCH("캠프 로그를 쓴 작성자만 수정이 가능합니다.", HttpStatus.FORBIDDEN),
    MEMBER_SIGN_UP_ERROR("회원가입이 정상적으로 이루어지지 않았습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;

}
