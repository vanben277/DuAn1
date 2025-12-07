package com.example.baitap1be.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RegisterResponse {
    private String userCode;
    private String fullName;
    private String role;
}
