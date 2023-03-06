package com.camping101.beta.exception;

import com.camping101.beta.member.exception.MemberException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MemberException.class)
    public ResponseEntity<ExceptionResponse> handleMemberException(MemberException e) {

        var errorCode = e.getErrorCode();
        var errorResponse = ExceptionResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .reasonOfError(errorCode.getHttpStatus().getReasonPhrase())
                .errorMessage(errorCode.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }


}
