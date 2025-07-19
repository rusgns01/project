package com.example.crud.exception;

public class FollowException extends RuntimeException {
    private final Errorcode errorCode;

    public FollowException(Errorcode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
