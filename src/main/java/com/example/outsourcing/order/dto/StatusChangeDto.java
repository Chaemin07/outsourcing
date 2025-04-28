package com.example.outsourcing.order.dto;

import com.example.outsourcing.common.enums.DeliveryStatus;
import com.example.outsourcing.common.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class StatusChangeDto {
    private Long storeId;                // 가게 ID
    private Long orderId;                // 주문 ID
    private OrderStatus orderStatus;     // 주문 상태
    private DeliveryStatus deliveryStatus; // 배달 상태
}
