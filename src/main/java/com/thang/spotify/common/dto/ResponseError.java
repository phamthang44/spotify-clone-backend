package com.thang.spotify.common.dto;

public class ResponseError extends ResponseData<Void> {

    public ResponseError(int status, String message) {
        super(status, message);
    }

}
