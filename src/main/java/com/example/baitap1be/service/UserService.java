package com.example.baitap1be.service;

import com.example.baitap1be.dto.req.LoginRequest;
import com.example.baitap1be.dto.req.LogoutRequest;
import com.example.baitap1be.dto.res.LoginResponse;
import jakarta.validation.Valid;

public interface UserService {
    LoginResponse login(@Valid LoginRequest form);


    void logout(String token);
}
