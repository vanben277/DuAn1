package com.example.baitap1be.controller;

import com.example.baitap1be.dto.ApiResponse;
import com.example.baitap1be.dto.res.StoreResponse;
import com.example.baitap1be.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/stores")
public class StoreController {
    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllStores() {
        List<StoreResponse> list = storeService.getAllStores();
        return ResponseEntity.ok(new ApiResponse("Lấy danh sách cửa hàng thành công", list));
    }
}
