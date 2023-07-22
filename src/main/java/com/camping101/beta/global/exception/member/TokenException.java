package com.camping101.beta.global.exception.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenException extends RuntimeException {

    private final ErrorCode errorCode;

}
