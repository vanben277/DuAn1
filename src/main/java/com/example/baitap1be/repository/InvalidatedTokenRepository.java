package com.example.baitap1be.repository;

import com.example.baitap1be.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
    @Modifying
    @Query("DELETE FROM InvalidatedToken i WHERE i.expiryTime < :now")
    int deleteByExpiryTimeBefore(@Param("now") Date now);
}
