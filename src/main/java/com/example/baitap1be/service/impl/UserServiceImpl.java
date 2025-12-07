package com.example.baitap1be.service.impl;

import com.example.baitap1be.dto.req.FilterRequest;
import com.example.baitap1be.dto.res.FilterResponse;
import com.example.baitap1be.entity.User;
import com.example.baitap1be.repository.UserRepository;
import com.example.baitap1be.repository.specification.UserSpecification;
import com.example.baitap1be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<FilterResponse> getUserByFilters(FilterRequest filterRequest) {
        Specification<User> specification = UserSpecification.hasStoreCode(filterRequest.getStoreCode())
                .and(UserSpecification.fullNameContains(filterRequest.getFullName()))
                .and(UserSpecification.hasRole(filterRequest.getRole()))
                .and(UserSpecification.isActive());

        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<User> page = userRepository.findAll(specification, pageable);

        return page.map(user -> modelMapper.map(user, FilterResponse.class));
    }
}
