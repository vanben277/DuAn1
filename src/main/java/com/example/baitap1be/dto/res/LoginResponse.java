package com.example.baitap1be.dto.res;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String userCode;
    private String fullName;
    private String role;
    private String storeName;
}
