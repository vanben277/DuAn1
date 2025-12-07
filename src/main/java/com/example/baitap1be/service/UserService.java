package com.example.baitap1be.service;

import com.example.baitap1be.dto.req.FilterRequest;
import com.example.baitap1be.dto.res.FilterResponse;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<FilterResponse> getUserByFilters(FilterRequest filterRequest);
}
