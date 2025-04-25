package com.example.outsourcing.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CategoryType {
    CHICKEN, // 치킨
    PIZZA, // 피자
    KOREAN, // 한식
    CHINESE, // 중식
    CAFE, // 카페
    JAPANESE, // 일식
    SNACK_FOOD, // 분식
    ASIAN, // 아시안
    WESTERN, // 양식
    LATE_NIGHT_SNACK, // 야식
    MEAT; // 고기

    @JsonCreator
    public static CategoryType from(String value) {
        return CategoryType.valueOf(value.toUpperCase());
    }
}
