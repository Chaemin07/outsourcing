package com.example.outsourcing.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    // DB에서 상태는 Enum으로, 응답은 String으로
    private String status;
    private Integer totalPrice;
    private String message;
}
