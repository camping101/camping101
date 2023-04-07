package com.camping101.beta.web.domain.token.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorResponse {

    private final String errorCode;
    private final String errorMessage;

}
