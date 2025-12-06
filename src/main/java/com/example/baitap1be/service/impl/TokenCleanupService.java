package com.example.baitap1be.service.impl;

import com.example.baitap1be.repository.InvalidatedTokenRepository;
import com.example.baitap1be.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class TokenCleanupService {
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void cleanupExpiredAccessTokens() {
        Date now = new Date();
        int deletedCount = invalidatedTokenRepository.deleteByExpiryTimeBefore(now);
        System.out.println("Đã xóa " + deletedCount + " access tokens hết hạn");
    }

    @Scheduled(cron = "0 0 2 * * *") // xóa vào 2h sáng
    @Transactional
    public void cleanupExpiredRefreshTokens() {
        Date now = new Date();
        int deletedCount = refreshTokenRepository.deleteByExpiryDateBefore(now);
        System.out.println("Đã xóa " + deletedCount + " refresh tokens hết hạn");
    }
}
