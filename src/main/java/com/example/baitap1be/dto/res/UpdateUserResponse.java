package com.example.baitap1be.dto.res;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserResponse {
    private String userCode;
    private String fullName;
    private String email;
    private String role;
    private String storeCode;
    private String storeName;
    private Boolean isActive;
    private LocalDateTime updatedAt;
}
