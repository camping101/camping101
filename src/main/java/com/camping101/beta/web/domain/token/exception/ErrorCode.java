package com.camping101.beta.web.domain.token.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_REFRESH_TOKEN("잘못된 리프레시 토큰입니다.", HttpStatus.BAD_REQUEST),
    INVALID_ACCESS_TOKEN("잘못된 엑세스 토큰입니다.", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_NOT_FOUND_IN_REDIS("리플래시 토큰을 레디스에서 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

}
