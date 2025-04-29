package com.example.outsourcing.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderStatusUpdateRequest {
    @NotBlank(message = "상태 값은 필수입니다.")
    private String status;
    private String message;
}
