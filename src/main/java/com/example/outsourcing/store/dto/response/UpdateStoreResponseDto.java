package com.example.outsourcing.store.dto.response;

import com.example.outsourcing.store.entity.Store;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateStoreResponseDto {

    private final Long id;

    private final String name;

    private final String status;

    private final String storePhoneNumber;

    private final Integer minOrderPrice;

    private final String openingTimes;

    private final String closingTimes;

    private final String notification;

    private final LocalDateTime updatedAt;

    public static UpdateStoreResponseDto toDto(Store store) {
        return new UpdateStoreResponseDto(
            store.getId(),
            store.getName(),
            store.getStatus().name(),
            store.getStorePhoneNumber(),
            store.getMinOrderPrice(),
            store.getOpeningTimes(),
            store.getClosingTimes(),
            store.getNotification(),
            store.getUpdatedAt()
        );
    }

}
