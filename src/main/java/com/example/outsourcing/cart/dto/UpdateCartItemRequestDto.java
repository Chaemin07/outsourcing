package com.example.outsourcing.cart.dto;

import lombok.AllArgsConstructor;

import java.util.List;


@AllArgsConstructor
public class UpdateCartItemRequestDto {
    private Long menuId;
    private Integer quantity;
    private List<Long> optionIds;
}
