package com.example.outsourcing.store.dto.response;

import com.example.outsourcing.menu.dto.response.MenuSummaryResponseDto;
import com.example.outsourcing.store.entity.Store;
import com.example.outsourcing.store.util.StoreStatusUtil;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetStoreWithMenuResponseDto {

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

    private final List<MenuSummaryResponseDto> menus;

    public static GetStoreWithMenuResponseDto toDto(Store store, List<MenuSummaryResponseDto> menus) {

        boolean isOpen = StoreStatusUtil.isOpen(store.getOpeningTimes(), store.getClosingTimes());

        return new GetStoreWithMenuResponseDto(
            store.getId(),
            store.getName(),
            store.getAddress().getAddress(),
            store.getStatus().name(),
            store.getStorePhoneNumber(),
            store.getMinOrderPrice(),
            store.getOpeningTimes(),
            store.getClosingTimes(),
            isOpen,
            store.getNotification(),
            store.getCreatedAt(),
            store.getUpdatedAt(),
            menus
        );
    }

}
