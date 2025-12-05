package com.example.baitap1be.service.impl;

import com.example.baitap1be.dto.req.LoginRequest;
import com.example.baitap1be.dto.req.LogoutRequest;
import com.example.baitap1be.dto.res.LoginResponse;
import com.example.baitap1be.entity.InvalidatedToken;
import com.example.baitap1be.entity.User;
import com.example.baitap1be.enums.Role;
import com.example.baitap1be.exception.*;
import com.example.baitap1be.repository.InvalidatedTokenRepository;
import com.example.baitap1be.repository.UserRepository;
import com.example.baitap1be.security.JwtUtil;
import com.example.baitap1be.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest form) {
        User user = userRepository.findByUserCode(form.getUserCode())
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.USER_NOT_FOUND, "Tài khoản không tồn tại"));

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new ForbiddenException(ErrorCodeConstant.USER_LOCKED, "Tài khoản đã bị khóa");
        }

        if (!passwordEncoder.matches(form.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException(ErrorCodeConstant.LOGIN_FAIL, "Sai mật khẩu");
        }

        if (!Role.ADMIN.equals(user.getRole())) {
            if (user.getStore() == null) {
                throw new ForbiddenException(ErrorCodeConstant.DATA_ERROR, "Tài khoản nhân viên này chưa được gán vào cửa hàng nào");
            }

            String inputStoreCode = form.getStoreCode();

            if (inputStoreCode == null || inputStoreCode.trim().isEmpty()) {
                throw new ForbiddenException(ErrorCodeConstant.ARGUMENT_NOT_VALID, "Vui lòng nhập Store ID");
            }

            if (!form.getStoreCode().equals(user.getStore().getStoreCode())) {
                throw new ForbiddenException(ErrorCodeConstant.LOGIN_FAIL, "Tài khoản không thuộc Store ID này");
            }
        }

        String token = jwtUtil.generateToken(user);

        return LoginResponse.builder()
                .token(token)
                .userCode(user.getUserCode())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .storeName(user.getStore() != null ? user.getStore().getStoreName() : "N/A")
                .build();
    }

    @Override
    @Transactional
    public void logout(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new BusinessException(ErrorCodeConstant.ARGUMENT_NOT_VALID, "Token không hợp lệ");
        }

        if (!jwtUtil.validateToken(token)) {
            throw new UnauthorizedException(ErrorCodeConstant.INVALID_TOKEN, "Token không hợp lệ hoặc đã hết hạn");
        }

        if (invalidatedTokenRepository.existsById(token)) {
            return;
        }

        Date expiryTime = jwtUtil.extractClaim(token, Claims::getExpiration);

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(token)
                .expiryTime(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }
}
