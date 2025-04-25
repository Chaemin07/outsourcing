package com.example.outsourcing.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderStatusUpdateRequest {
    private String status;
    private String message;
}
