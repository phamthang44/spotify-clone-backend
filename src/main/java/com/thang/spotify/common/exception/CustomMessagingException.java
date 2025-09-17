package com.thang.spotify.common.exception;

import com.thang.spotify.common.enums.ErrorCode;

public class CustomMessagingException extends AppException {
    public CustomMessagingException(ErrorCode errorCode, String messageArg) {
        super(errorCode, messageArg);
    }
}
