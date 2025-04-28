package com.example.outsourcing.order.entity;

import lombok.Getter;

@Getter
public enum DeliveryStatus {
    PREPARED("배달 준비중"),       // 배달 준비중(음식 조리는 완료)
    DELIVERING("배달 중"),     // 배달 중
    DELIVERED("배달 완료");       // 배달 완료

    private final String value;

    DeliveryStatus(String value) {
        this.value = value;
    }

}
