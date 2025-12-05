package com.example.baitap1be.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "user id không được bỏ trống")
    private String userCode;
    @NotBlank(message = "password không được bỏ trống")
    private String password;
    private String storeCode;
}
