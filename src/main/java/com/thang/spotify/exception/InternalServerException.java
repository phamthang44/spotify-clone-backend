package com.thang.spotify.exception;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String failedToRegisterUser) {
        super("An internal server error occurred. Please try again later.");
    }
}
