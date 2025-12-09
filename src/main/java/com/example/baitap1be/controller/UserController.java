package com.example.baitap1be.controller;

import com.example.baitap1be.dto.ApiResponse;
import com.example.baitap1be.dto.req.FilterRequest;
import com.example.baitap1be.dto.req.RegisterRequest;
import com.example.baitap1be.dto.req.UpdateUserRequest;
import com.example.baitap1be.dto.res.FilterResponse;
import com.example.baitap1be.dto.res.RegisterResponse;
import com.example.baitap1be.dto.res.UpdateUserResponse;
import com.example.baitap1be.dto.res.UserDetailResponse;
import com.example.baitap1be.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/filters")
    public ResponseEntity<ApiResponse> getUserByFilters(@ModelAttribute FilterRequest filterRequest) {
        Page<FilterResponse> filterResponses = userService.getUserByFilters(filterRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Lấy danh sách thành công", filterResponses));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        RegisterResponse user = userService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Đăng ký thành công", user));
    }

    @GetMapping("/roles")
    public ResponseEntity<ApiResponse> getRoles() {
        List<String> list = userService.getRoles();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Lấy danh sách role thành công", list));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Integer id, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        UpdateUserResponse user = userService.updateUser(id, updateUserRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Cập nhật thành công", user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUserById(@PathVariable Integer id) {
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserDetail(@PathVariable Integer id) {
        UserDetailResponse response = userService.getUserDetail(id);
        return ResponseEntity.ok(new ApiResponse("Lấy thông tin người dùng thành công", response));
    }
}
