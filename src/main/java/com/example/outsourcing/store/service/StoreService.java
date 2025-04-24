package com.example.outsourcing.store.service;

import com.example.outsourcing.address.entity.Address;
import com.example.outsourcing.image.entity.Image;
import com.example.outsourcing.store.dto.request.CreateStoreRequestDto;
import com.example.outsourcing.store.dto.response.CreateStoreResponseDto;
import com.example.outsourcing.store.entity.Store;
import com.example.outsourcing.store.repository.StoreRepository;
import com.example.outsourcing.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    @Transactional
    public CreateStoreResponseDto createStore(String name, String status, String storePhoneNumber,
        Integer minOderPrice, String openingTimes, String closingTimes, String notification) {

        Store store = new Store(name, status, storePhoneNumber,
            minOderPrice, openingTimes, closingTimes, notification);

        Store savedStore = storeRepository.save(store);

        return new CreateStoreResponseDto(savedStore.getId(), savedStore.getName(),
            savedStore.getStatus(), savedStore.getStorePhoneNumber(),
            savedStore.getMinOderPrice(), savedStore.getOpeningTimes(),
            savedStore.getClosingTimes(), savedStore.getNotification(), savedStore.getCreatedAt(),
            savedStore.getUpdatedAt());
    }

}
