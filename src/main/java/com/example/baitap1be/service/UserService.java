package com.example.baitap1be.service;

import com.example.baitap1be.dto.req.FilterRequest;
import com.example.baitap1be.dto.req.RegisterRequest;
import com.example.baitap1be.dto.req.UpdateUserRequest;
import com.example.baitap1be.dto.res.FilterResponse;
import com.example.baitap1be.dto.res.RegisterResponse;
import com.example.baitap1be.dto.res.UpdateUserResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    Page<FilterResponse> getUserByFilters(FilterRequest filterRequest);

    RegisterResponse register(@Valid RegisterRequest registerRequest);

    List<String> getRoles();

    UpdateUserResponse updateUser(Integer id, UpdateUserRequest updateUserRequest);

    void deleteUserById(Integer id);
}
