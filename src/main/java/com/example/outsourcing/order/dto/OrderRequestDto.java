package com.example.outsourcing.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderRequestDto {
    @Size(max = 255, message = "요청 메시지는 255자를 초과할 수 없습니다.")
    private String requestMessage;
    @NotBlank(message = "배달 주소는 필수입니다.")
    private String deliveryAddress;
    @NotEmpty(message = "주문 항목은 최소 1개 이상이어야 합니다.")
    @Valid
    private List<OrderItemRequestDto> orderItems;

}
