package com.example.baitap1be.repository;

import com.example.baitap1be.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Integer> {
    List<Store> findAllByIsActiveTrue();
}
