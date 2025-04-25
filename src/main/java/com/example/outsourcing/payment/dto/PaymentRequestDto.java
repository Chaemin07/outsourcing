package com.example.outsourcing.payment.dto;

import com.example.outsourcing.payment.entity.PaymentMethod;
import lombok.Getter;

@Getter
public class PaymentRequestDto {
    private Long orderId;
    private PaymentMethod paymentMethod;
    private Integer inputAmount ;
}
