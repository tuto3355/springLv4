package com.nakta.springlv1.global.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {

//    private final String errorName;
    private final String message;

    public ErrorResponse(ErrorCode errorCode) {
//        this.errorName = errorCode.name();
        this.message = errorCode.getMessage();
    }
}
