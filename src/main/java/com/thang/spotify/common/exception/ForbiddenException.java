package com.thang.spotify.common.exception;

import com.thang.spotify.common.enums.ErrorCode;

public class ForbiddenException extends AppException {
    public ForbiddenException(String target) {
        super(ErrorCode.FORBIDDEN, target);
    }
}