package com.camping101.beta.web.domain.bookMark.exception;

import lombok.Getter;

@Getter
public class BookMarkException extends RuntimeException{

    private final ErrorCode errorCode;

    public BookMarkException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public BookMarkException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BookMarkException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public BookMarkException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public BookMarkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }
}
