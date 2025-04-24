package com.example.outsourcing.store.dto.request;

import com.example.outsourcing.address.entity.Address;
import com.example.outsourcing.image.entity.Image;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateStoreRequestDto {

    private final String name;

    private final String status;

    private final String storePhoneNumber;

    private final Integer minOrderPrice;

    private final String openingTimes;

    private final String closingTimes;

    private final String notification;
}
