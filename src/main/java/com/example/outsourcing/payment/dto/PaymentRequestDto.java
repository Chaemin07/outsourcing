package com.example.outsourcing.payment.dto;

import com.example.outsourcing.payment.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
public class PaymentRequestDto {
    @NotNull(message = "주문 ID는 필수입니다.")
    private Long orderId;
    @NotNull(message = "결제 수단은 필수입니다.")
    private PaymentMethod paymentMethod;
    @NotNull(message = "입력 금액은 필수입니다.")
    @PositiveOrZero(message = "입력 금액은 0 이상이어야 합니다.")
    private Integer inputAmount;
}
