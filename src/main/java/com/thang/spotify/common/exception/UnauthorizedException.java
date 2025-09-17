package com.thang.spotify.common.exception;

import com.thang.spotify.common.enums.ErrorCode;

public class UnauthorizedException extends AppException {
    public UnauthorizedException(String target) {
        super(ErrorCode.UNAUTHORIZED, target);
    }
}