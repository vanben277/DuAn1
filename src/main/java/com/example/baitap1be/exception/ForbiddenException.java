package com.example.baitap1be.exception;

public class ForbiddenException extends BusinessException {
    public ForbiddenException(String message, String errorCode) {
        super(message, errorCode);
    }
}
