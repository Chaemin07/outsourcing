package com.example.outsourcing.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UpdateCartItemRequestDto {
    private Long menuId;
    private Integer quantity;
    private List<Long> optionIdsToAdd;
    private List<Long> optionIdsToRemove;
}
