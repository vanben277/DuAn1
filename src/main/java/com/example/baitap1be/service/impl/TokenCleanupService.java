package com.example.baitap1be.service.impl;

import com.example.baitap1be.entity.InvalidatedToken;
import com.example.baitap1be.repository.InvalidatedTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class TokenCleanupService {
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void cleanupExpiredTokens() {
        Date now = new Date();
        int deletedCount = invalidatedTokenRepository.deleteByExpiryTimeBefore(now);
        System.out.println("Đã xóa " + deletedCount + " tokens hết hạn");
    }
}
