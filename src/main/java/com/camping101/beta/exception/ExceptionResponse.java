package com.camping101.beta.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class ExceptionResponse {

    private int status;
    private String reasonOfError;
    private String errorMessage;

}
