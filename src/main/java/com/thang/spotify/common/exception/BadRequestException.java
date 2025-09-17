package com.thang.spotify.common.exception;

import com.thang.spotify.common.enums.ErrorCode;

public class BadRequestException extends AppException{
    public BadRequestException(String what) {
        super(ErrorCode.BAD_REQUEST, what);
    }
}
