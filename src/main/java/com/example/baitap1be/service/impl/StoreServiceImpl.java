package com.example.baitap1be.service.impl;

import com.example.baitap1be.dto.res.StoreResponse;
import com.example.baitap1be.entity.Store;
import com.example.baitap1be.repository.StoreRepository;
import com.example.baitap1be.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;

    @Override
    public List<StoreResponse> getAllStores() {
        List<Store> list = storeRepository.findAllByIsActiveTrue();
        return list.stream()
                .map(store -> new StoreResponse(store.getStoreCode(), store.getStoreName()))
                .collect(Collectors.toList());
    }
}
