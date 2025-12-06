package com.example.baitap1be.service.impl;

import com.example.baitap1be.dto.req.LoginRequest;
import com.example.baitap1be.dto.req.RefreshTokenRequest;
import com.example.baitap1be.dto.res.LoginResponse;
import com.example.baitap1be.entity.InvalidatedToken;
import com.example.baitap1be.entity.RefreshToken;
import com.example.baitap1be.entity.User;
import com.example.baitap1be.enums.Role;
import com.example.baitap1be.exception.ErrorCodeConstant;
import com.example.baitap1be.exception.ForbiddenException;
import com.example.baitap1be.exception.NotFoundException;
import com.example.baitap1be.exception.UnauthorizedException;
import com.example.baitap1be.repository.InvalidatedTokenRepository;
import com.example.baitap1be.repository.RefreshTokenRepository;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private final long REFRESH_EXPIRATION = 7L * 24 * 60 * 60 * 1000; // 7 ngày

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
                throw new ForbiddenException(ErrorCodeConstant.DATA_ERROR,
                        "Tài khoản nhân viên này chưa được gán vào cửa hàng nào");
            }

            String inputStoreCode = form.getStoreCode();
            if (inputStoreCode == null || inputStoreCode.trim().isEmpty()) {
                throw new ForbiddenException(ErrorCodeConstant.ARGUMENT_NOT_VALID, "Vui lòng nhập Store ID");
            }

            if (!form.getStoreCode().equals(user.getStore().getStoreCode())) {
                throw new ForbiddenException(ErrorCodeConstant.LOGIN_FAIL,
                        "Tài khoản không thuộc Store ID này");
            }
        }

        String accessToken = jwtUtil.generateToken(user);

        String refreshTokenStr = UUID.randomUUID().toString();

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .token(refreshTokenStr)
                .user(user)
                .expiryDate(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .build();

        refreshTokenRepository.save(refreshTokenEntity);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenStr)
                .userCode(user.getUserCode())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .storeName(user.getStore() != null ? user.getStore().getStoreName() : "N/A")
                .build();
    }

    @Override
    @Transactional
    public LoginResponse refresh(RefreshTokenRequest request) {
        RefreshToken rt = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new UnauthorizedException(ErrorCodeConstant.INVALID_TOKEN,
                        "Refresh token không tồn tại"));

        if (rt.getExpiryDate().before(new Date())) {
            refreshTokenRepository.delete(rt);
            throw new UnauthorizedException(ErrorCodeConstant.INVALID_TOKEN,
                    "Refresh token đã hết hạn, vui lòng đăng nhập lại");
        }

        User user = rt.getUser();

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new ForbiddenException(ErrorCodeConstant.USER_LOCKED, "Tài khoản đang bị khóa");
        }

        String newAccessToken = jwtUtil.generateToken(user);

        String newRefreshTokenStr = UUID.randomUUID().toString();
        RefreshToken newRefreshToken = RefreshToken.builder()
                .token(newRefreshTokenStr)
                .user(user)
                .expiryDate(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .build();

        refreshTokenRepository.delete(rt);
        refreshTokenRepository.save(newRefreshToken);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshTokenStr)
                .userCode(user.getUserCode())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .storeName(user.getStore() != null ? user.getStore().getStoreName() : "N/A")
                .build();
    }

    @Override
    @Transactional
    public void logout(String accessToken, String refreshToken) {
        if (accessToken != null && jwtUtil.validateToken(accessToken)) {
            if (!invalidatedTokenRepository.existsById(accessToken)) {
                Date expiryTime = jwtUtil.extractClaim(accessToken, Claims::getExpiration);
                invalidatedTokenRepository.save(
                        InvalidatedToken.builder()
                                .id(accessToken)
                                .expiryTime(expiryTime)
                                .build()
                );
            }
        }

        if (refreshToken != null) {
            refreshTokenRepository.deleteByToken(refreshToken);
        }
    }

    @Override
    @Transactional
    public void logoutAllDevices(String userCode) {
        User user = userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.USER_NOT_FOUND,
                        "Tài khoản không tồn tại"));

        refreshTokenRepository.deleteByUser_Id(user.getId());

        user.setTokenVersion(user.getTokenVersion() + 1);
        userRepository.save(user);
    }
}
