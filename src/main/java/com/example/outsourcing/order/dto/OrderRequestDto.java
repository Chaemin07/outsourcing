package com.example.outsourcing.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderRequestDto {

    private String requestMessage;

    private String deliveryAddress;

    private List<OrderItemRequestDto> orderItems;

}
