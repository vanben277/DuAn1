package com.example.baitap1be.dto.req;

import com.example.baitap1be.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequest {
    private String storeCode;
    private String fullName;
    private Role role;
}
