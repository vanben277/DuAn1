package com.example.baitap1be.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterResponse {
    private String userCode;
    private String fullName;
    private String storeCode;
    private String storeName;
    private String role;
}
