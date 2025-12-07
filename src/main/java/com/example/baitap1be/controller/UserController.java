package com.example.baitap1be.controller;

import com.example.baitap1be.dto.ApiResponse;
import com.example.baitap1be.dto.req.FilterRequest;
import com.example.baitap1be.dto.res.FilterResponse;
import com.example.baitap1be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
