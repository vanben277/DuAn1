package com.example.baitap1be.exception;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(String message, String errorCode) {
        super(message, errorCode);
    }
}
