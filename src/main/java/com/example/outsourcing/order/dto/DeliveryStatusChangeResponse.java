package com.example.outsourcing.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DeliveryStatusChangeResponse {
    private Long orderId;
    private String previousDeliveryStatus;
    private String newDeliveryStatus;
}
