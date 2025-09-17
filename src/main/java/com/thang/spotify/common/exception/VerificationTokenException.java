package com.thang.spotify.common.exception;

import com.thang.spotify.common.enums.ErrorCode;

public class VerificationTokenException extends AppException {
    public VerificationTokenException(ErrorCode errorCode, String messageArg) {
        super(errorCode, messageArg);
    }

    public VerificationTokenException(ErrorCode errorCode, Object... messageArgs) {
        super(errorCode, messageArgs);
    }
}
