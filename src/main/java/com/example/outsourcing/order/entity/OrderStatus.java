package com.example.outsourcing.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("주문 대기"),            // 주문 대기 중(사장님이 수락 전)
    ACCEPTED("주문 수락"),           // 주문 수락됨(사장님이 수락)
    REJECTED("주문 거절"),           // 주문 거절됨(사장님이 거절)
    CANCELLED("사용자 취소"),        // 사용자 취소
    PREPARING("음식 준비 중"),       // 음식 준비 중
    COMPLETED("음식 준비 완료");       // 음식 준비 완료 (배달 대기)

    private final String value;

    OrderStatus(String value){
        this.value = value;
    }

}
