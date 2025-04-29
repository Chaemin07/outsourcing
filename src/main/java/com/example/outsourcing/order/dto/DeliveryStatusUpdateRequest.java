package com.example.outsourcing.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DeliveryStatusUpdateRequest {
    // Enum 값 (PREPARED, DELIVERING, DELIVERED)
     @NotBlank(message = "상태 값은 필수입니다.")
    private String deliveryStatus;
}