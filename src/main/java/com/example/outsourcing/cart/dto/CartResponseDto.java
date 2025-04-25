package com.example.outsourcing.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class CartResponseDto {
    private List<CartItemDto> items = new ArrayList<>();
    private Integer totalPrice;
}
