package com.example.baitap1be.dto.res;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private String userCode;
    private String fullName;
    private String role;
}
