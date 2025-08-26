package com.thang.spotify.exception;

import com.thang.spotify.common.enums.ErrorCode;

public class TokenNotFoundException extends AppException {

    public TokenNotFoundException(ErrorCode errorCode, String messageArg) {
        super(errorCode, messageArg);
    }

    public TokenNotFoundException(ErrorCode errorCode, Object... messageArgs) {
        super(errorCode, messageArgs);
    }
}
