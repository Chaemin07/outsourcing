package com.example.outsourcing.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateStoreRequestDto {

    private final String name;

    @NotBlank(message = "매장전화번호는 필수값입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "유효한 메장전화번호 형식이 아닙니다.")
    private final String storePhoneNumber;

    private final Integer minOrderPrice;

    @NotBlank(message = "오픈 시간은 필수값입니다.")
    @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$", message = "유효한 오픈 시간 형식이 아닙니다.")
    private final String openingTimes;

    @NotBlank(message = "마감 시간은 필수값입니다.")
    @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$", message = "유효한 마감 시간 형식이 아닙니다.")
    private final String closingTimes;

    private final String notification;
}
