package com.camping101.beta.web.domain.token.exception;

import lombok.Getter;

@Getter
public class TokenException extends RuntimeException{

    private final ErrorCode errorCode;

    public TokenException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public TokenException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public TokenException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public TokenException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public TokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }
}
