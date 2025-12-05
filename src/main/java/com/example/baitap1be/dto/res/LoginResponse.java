package com.example.baitap1be.dto.res;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private String userCode;
    private String fullName;
    private String role;
    private String storeName;
}
