package com.thang.spotify.exception;

import com.thang.spotify.common.enums.ErrorCode;

public class ForbiddenException extends AppException {
    public ForbiddenException(String target) {
        super(ErrorCode.FORBIDDEN, target);
    }
}