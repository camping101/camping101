package com.camping101.beta.exception;

import com.camping101.beta.admin.recTag.exception.RecTagException;
import com.camping101.beta.campLog.exception.CampLogException;
import com.camping101.beta.comment.exception.CommentException;
import com.camping101.beta.member.exception.MemberException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MemberException.class)
    public ResponseEntity<ExceptionResponse> handleMemberException(MemberException e) {

        com.camping101.beta.member.exception.ErrorCode errorCode = e.getErrorCode();
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .reasonOfError(errorCode.getHttpStatus().getReasonPhrase())
                .errorMessage(errorCode.getMessage())
                .build();

        return new ResponseEntity<>(exceptionResponse, errorCode.getHttpStatus());
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .status(403)
                .reasonOfError("로그인이 정상적으로 이루어지지 않았습니다.")
                .errorMessage(e.getMessage())
                .build();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = CommentException.class)
    public ResponseEntity<ExceptionResponse> handleCommentException(CommentException e) {

        com.camping101.beta.comment.exception.ErrorCode errorCode = e.getErrorCode();
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .reasonOfError(errorCode.getHttpStatus().getReasonPhrase())
                .errorMessage(errorCode.getMessage())
                .build();

        return new ResponseEntity<>(exceptionResponse, errorCode.getHttpStatus());
    }

    @ExceptionHandler(value = RecTagException.class)
    public ResponseEntity<ExceptionResponse> handleRecTagException(RecTagException e) {

        com.camping101.beta.admin.recTag.exception.ErrorCode errorCode = e.getErrorCode();
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .reasonOfError(errorCode.getHttpStatus().getReasonPhrase())
                .errorMessage(errorCode.getMessage())
                .build();

        return new ResponseEntity<>(exceptionResponse, errorCode.getHttpStatus());
    }

    @ExceptionHandler(value = CampLogException.class)
    public ResponseEntity<ExceptionResponse> handleCampLogException(CampLogException e) {

        com.camping101.beta.campLog.exception.ErrorCode errorCode = e.getErrorCode();
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .reasonOfError(errorCode.getHttpStatus().getReasonPhrase())
                .errorMessage(errorCode.getMessage())
                .build();

        return new ResponseEntity<>(exceptionResponse, errorCode.getHttpStatus());
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .status(500)
                .reasonOfError("데이터베이스 서버 관련 이슈가 발생했습니다.")
                .errorMessage(e.getMessage())
                .build();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
