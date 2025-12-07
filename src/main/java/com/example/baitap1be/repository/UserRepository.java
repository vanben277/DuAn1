package com.example.baitap1be.repository;

import com.example.baitap1be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    boolean existsByUserCode(String userCode);

    boolean existsByEmail(String email);
}
