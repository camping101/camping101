package com.camping101.beta.admin.recTag.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorResponse {

    private final String code;
    private final String message;

}
