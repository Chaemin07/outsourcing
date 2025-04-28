package com.example.outsourcing.store.dto.response;

import com.example.outsourcing.store.entity.Store;
import com.example.outsourcing.store.util.StoreStatusUtil;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateStoreResponseDto {

    private final Long id;

    private final String name;

    private final String address;

    private final String status;

    private final String storePhoneNumber;

    private final Integer minOrderPrice;

    private final String openingTimes;

    private final String closingTimes;

    private final boolean isOpen;

    private final String notification;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    public static CreateStoreResponseDto toDto(Store store) {

        boolean isOpen = StoreStatusUtil.isOpen(store.getOpeningTimes(), store.getClosingTimes());

        return new CreateStoreResponseDto(
            store.getId(),
            store.getName(),
            store.getAddress(),
            store.getStatus().name(),
            store.getStorePhoneNumber(),
            store.getMinOrderPrice(),
            store.getOpeningTimes(),
            store.getClosingTimes(),
            isOpen,
            store.getNotification(),
            store.getCreatedAt(),
            store.getUpdatedAt()
        );
    }
}
