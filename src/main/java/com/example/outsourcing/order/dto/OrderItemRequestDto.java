package com.example.outsourcing.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderItemRequestDto {
    private Long menuId;
    private Integer quantity;
    private List<Long> optionIds;
}
