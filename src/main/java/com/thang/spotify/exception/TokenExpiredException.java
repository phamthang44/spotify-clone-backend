package com.thang.spotify.exception;

import com.thang.spotify.common.enums.ErrorCode;

public class TokenExpiredException extends AppException {
    public TokenExpiredException(ErrorCode errorCode, String messageArg) {
        super(errorCode, messageArg);
    }

    public TokenExpiredException(ErrorCode errorCode, Object... messageArgs) {
        super(errorCode, messageArgs);
    }
}
