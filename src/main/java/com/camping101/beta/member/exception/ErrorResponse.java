package com.camping101.beta.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorResponse {

    private final String code;
    private final String message;

}
