package com.example.baitap1be.service.impl;

import com.example.baitap1be.dto.req.FilterRequest;
import com.example.baitap1be.dto.req.RegisterRequest;
import com.example.baitap1be.dto.res.FilterResponse;
import com.example.baitap1be.dto.res.RegisterResponse;
import com.example.baitap1be.entity.Store;
import com.example.baitap1be.entity.User;
import com.example.baitap1be.enums.Role;
import com.example.baitap1be.exception.BusinessException;
import com.example.baitap1be.exception.ErrorCodeConstant;
import com.example.baitap1be.exception.ForbiddenException;
import com.example.baitap1be.exception.NotFoundException;
import com.example.baitap1be.repository.StoreRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final StoreRepository storeRepository;

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

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (Role.ADMIN.equals(request.getRole())) {
            throw new ForbiddenException(ErrorCodeConstant.ACCESS_DENIED, "Vui lòng truyền role là STAFF hoặc MANAGER");
        }

        if (userRepository.existsByUserCode(request.getUserCode())) {
            throw new BusinessException(ErrorCodeConstant.DATA_EXIST, "Mã nhân viên đã tồn tại");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCodeConstant.DATA_EXIST, "Email đã tồn tại trong hệ thống");
        }

        String storeCode = request.getStoreCode();
        if (storeCode == null || storeCode.trim().isEmpty()) {
            throw new BusinessException(ErrorCodeConstant.MISSING_PARAM, "Vui lòng chọn cửa hàng làm việc");
        }

        Store store = storeRepository.findByStoreCode(storeCode)
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.DATA_NOT_FOUND, "Mã cửa hàng không hợp lệ"));

        if (!Boolean.TRUE.equals(store.getIsActive())) {
            throw new BusinessException(ErrorCodeConstant.DATA_ERROR, "Cửa hàng này đang tạm ngưng hoạt động");
        }

        User newUser = new User();
        newUser.setUserCode(request.getUserCode());
        newUser.setFullName(request.getFullName());
        newUser.setEmail(request.getEmail());
        newUser.setRole(request.getRole());
        newUser.setStore(store);

        newUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        newUser.setIsActive(true);
        newUser.setTokenVersion(1);

        User save = userRepository.save(newUser);

        return modelMapper.map(save, RegisterResponse.class);
    }

    @Override
    public List<String> getRoles() {
        return Arrays.stream(Role.values())
                .filter(role -> !Role.ADMIN.equals(role))
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
