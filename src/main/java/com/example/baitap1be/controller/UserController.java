package com.example.baitap1be.controller;

import com.example.baitap1be.dto.ApiResponse;
import com.example.baitap1be.dto.req.LoginRequest;
import com.example.baitap1be.dto.req.LogoutRequest;
import com.example.baitap1be.dto.res.LoginResponse;
import com.example.baitap1be.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
@CrossOrigin(
        origins = {
                "http://localhost:5173"
        }
)
public class UserController {
    private final UserService userService;

    @PostMapping("login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest form) {
        LoginResponse response = userService.login(form);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Đăng nhập thành công!", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            userService.logout(token);
        }
        return ResponseEntity.ok(new ApiResponse("Đăng xuất thành công!", null));
    }
}
