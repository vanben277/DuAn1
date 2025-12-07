package com.example.baitap1be.service;

import com.example.baitap1be.dto.req.LoginRequest;
import com.example.baitap1be.dto.req.RefreshTokenRequest;
import com.example.baitap1be.dto.res.LoginResponse;
import jakarta.validation.Valid;

public interface AuthService {
    LoginResponse login(@Valid LoginRequest form);

    void logout(String accessToken, String refreshToken);

    LoginResponse refresh(RefreshTokenRequest request);

    void logoutAllDevices(String userCode);
}
