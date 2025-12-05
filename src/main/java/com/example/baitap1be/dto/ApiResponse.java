package com.example.baitap1be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    private String message;
    private Object data;
    private String errorMessage;

    public ApiResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }
}
