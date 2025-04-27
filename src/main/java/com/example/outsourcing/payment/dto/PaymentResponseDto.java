package com.example.outsourcing.payment.dto;

import com.example.outsourcing.payment.entity.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentResponseDto {
    private Long paymentId;
    private PaymentStatus paymentStatus;
    private Integer remainingBalance;
    private LocalDateTime paymentTime;
    private LocalDateTime cancelTime;
}
