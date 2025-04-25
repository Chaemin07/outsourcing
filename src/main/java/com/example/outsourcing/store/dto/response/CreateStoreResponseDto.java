package com.example.outsourcing.store.dto.response;

import com.example.outsourcing.menu.entity.Menu;
import com.example.outsourcing.store.entity.Store;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateStoreResponseDto {

    private final Long id;

    private final String name;

    private final String status;

    private final String storePhoneNumber;

    private final Integer minOrderPrice;

    private final String openingTimes;

    private final String closingTimes;

    private final String notification;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    public static CreateStoreResponseDto toDto(Store store) {
        return new CreateStoreResponseDto(
            store.getId(),
            store.getName(),
            store.getStatus(),
            store.getStorePhoneNumber(),
            store.getMinOrderPrice(),
            store.getOpeningTimes(),
            store.getClosingTimes(),
            store.getNotification(),
            store.getCreatedAt(),
            store.getUpdatedAt()
        );
    }
}
