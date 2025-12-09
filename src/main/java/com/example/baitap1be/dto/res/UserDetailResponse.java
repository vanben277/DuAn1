package com.example.baitap1be.dto.res;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailResponse {
    private Integer id;
    private String userCode;
    private String fullName;
    private String email;
    private String role;

    private String storeCode;
    private String storeName;

    private Boolean isActive;
}
