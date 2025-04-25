package com.example.outsourcing.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    // DB에서 상태는 Enum으로, 응답은 String으로
    private String status;
    private Integer totalPrice;
    private LocalDateTime createdAt;  // 생성일
    private LocalDateTime updatedAt;  // 수정일
}
