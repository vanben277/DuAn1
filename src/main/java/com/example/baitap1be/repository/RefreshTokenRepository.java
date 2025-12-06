package com.example.baitap1be.repository;

import com.example.baitap1be.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByUser_Id(Integer userId);

    void deleteByToken(String token);

    int deleteByExpiryDateBefore(Date now);
}
