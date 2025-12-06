package com.example.baitap1be.controller;

import com.example.baitap1be.dto.ApiResponse;
import com.example.baitap1be.dto.req.LoginRequest;
import com.example.baitap1be.dto.req.RefreshTokenRequest;
import com.example.baitap1be.dto.res.LoginResponse;
import com.example.baitap1be.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<ApiResponse> logout(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody(required = false) RefreshTokenRequest request
    ) {
        String accessToken = (authHeader != null && authHeader.startsWith("Bearer "))
                ? authHeader.substring(7) : null;

        String refreshToken = (request != null) ? request.getRefreshToken() : null;

        userService.logout(accessToken, refreshToken);
        return ResponseEntity.ok(new ApiResponse("Đăng xuất thành công!", null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        LoginResponse response = userService.refresh(request);
        return ResponseEntity.ok(new ApiResponse("Làm mới token thành công!", response));
    }

    @PostMapping("/logout-all")
    public ResponseEntity<ApiResponse> logoutAllDevices(@AuthenticationPrincipal UserDetails userDetails) {
        userService.logoutAllDevices(userDetails.getUsername());
        return ResponseEntity.ok(new ApiResponse("Đã đăng xuất tất cả thiết bị!", null));
    }
}
