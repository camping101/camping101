package com.camping101.beta.web.domain.admin.campAuth.exception;

import lombok.Getter;

@Getter
public class CampAuthException extends RuntimeException{

    private final ErrorCode errorCode;

    public CampAuthException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public CampAuthException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CampAuthException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public CampAuthException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public CampAuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }
}
