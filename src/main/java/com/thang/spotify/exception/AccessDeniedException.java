package com.thang.spotify.exception;

import com.thang.spotify.common.enums.ErrorCode;

public class AccessDeniedException extends AppException {
    public AccessDeniedException(ErrorCode errorCode, Object... messageArgs) {
        super(errorCode, messageArgs);
    }

    public AccessDeniedException(ErrorCode errorCode, String messageArg) {
        super(errorCode, messageArg);
    }
}
