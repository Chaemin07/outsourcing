package com.example.outsourcing.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderStatusChangeResponse {
    private Long orderId;
    private String previousOrderStatus;
    private String newOrderStatus;
}
