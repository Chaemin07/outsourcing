package com.example.outsourcing.order.dto;

import lombok.Getter;

@Getter
public class DeliveryStatusUpdateRequest {
    // Enum 값 (PREPARED, DELIVERING, DELIVERED)
    private String deliveryStatus;
}