package com.example.baitap1be.repository;

import com.example.baitap1be.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Integer> {
    List<Store> findAllByIsActiveTrue();

    Optional<Store> findByStoreCode(String storeCode);
}
