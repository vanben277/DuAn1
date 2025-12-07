package com.example.baitap1be.service.impl;

import com.example.baitap1be.dto.req.FilterRequest;
import com.example.baitap1be.dto.req.RegisterRequest;
import com.example.baitap1be.dto.req.UpdateUserRequest;
import com.example.baitap1be.dto.res.FilterResponse;
import com.example.baitap1be.dto.res.RegisterResponse;
import com.example.baitap1be.dto.res.UpdateUserResponse;
import com.example.baitap1be.entity.Store;
import com.example.baitap1be.entity.User;
import com.example.baitap1be.enums.Role;
import com.example.baitap1be.exception.BusinessException;
import com.example.baitap1be.exception.ErrorCodeConstant;
import com.example.baitap1be.exception.ForbiddenException;
import com.example.baitap1be.exception.NotFoundException;
import com.example.baitap1be.repository.RefreshTokenRepository;
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
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final RefreshTokenRepository refreshTokenRepository;

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

    @Override
    @Transactional
    public UpdateUserResponse updateUser(Integer id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.USER_NOT_FOUND, "Người dùng không tồn tại"));

        if (Role.ADMIN.equals(request.getRole())) {
            throw new ForbiddenException(ErrorCodeConstant.ACCESS_DENIED, "Không được phép cấp quyền Admin");
        }

        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCodeConstant.DATA_EXIST, "Email đã được sử dụng bởi tài khoản khác");
        }

        Store store = null;
        if (!Role.ADMIN.equals(request.getRole())) {
            String storeCode = request.getStoreCode();

            if (storeCode == null || storeCode.trim().isEmpty()) {
                throw new BusinessException(ErrorCodeConstant.MISSING_PARAM, "Vui lòng chọn cửa hàng làm việc");
            }

            store = storeRepository.findByStoreCode(storeCode)
                    .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.DATA_NOT_FOUND, "Mã cửa hàng không hợp lệ"));

            if (!Boolean.TRUE.equals(store.getIsActive())) {
                throw new BusinessException(ErrorCodeConstant.DATA_ERROR, "Cửa hàng này đang tạm ngưng hoạt động");
            }
        }

        boolean needForceLogout = !user.getRole().equals(request.getRole())
                || (request.getIsActive() != null && !request.getIsActive().equals(user.getIsActive()))
                || !user.getEmail().equals(request.getEmail());

        if (needForceLogout) {
            user.setTokenVersion(user.getTokenVersion() + 1);
            refreshTokenRepository.deleteByUser_Id(user.getId());
        }

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setStore(store);

        if (request.getIsActive() != null) {
            user.setIsActive(request.getIsActive());
        }

        User updatedUser = userRepository.save(user);

        return modelMapper.map(updatedUser, UpdateUserResponse.class);
    }

    @Override
    @Transactional
    public void deleteUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.USER_NOT_FOUND, "Người dùng không tồn tại"));

        String currentUserCode = SecurityContextHolder.getContext().getAuthentication().getName();
        if (user.getUserCode().equals(currentUserCode)) {
            throw new BusinessException(ErrorCodeConstant.ACCESS_DENIED, "Bạn không thể xóa chính tài khoản đang đăng nhập");
        }

        if (Role.ADMIN.equals(user.getRole())) {
            throw new ForbiddenException(ErrorCodeConstant.ACCESS_DENIED, "Không thể xóa tài khoản Quản trị viên");
        }

        user.setIsActive(false);

        refreshTokenRepository.deleteByUser_Id(id);

        user.setTokenVersion(user.getTokenVersion() + 1);

        userRepository.save(user);
    }
}
