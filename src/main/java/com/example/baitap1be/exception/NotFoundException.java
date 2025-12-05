package com.example.baitap1be.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundException extends BusinessException {
    public NotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }
}
