package com.example.outsourcing.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CartRequestDto {
    private Long storeId;
    private Long menuId;
    private Integer quantity;
    private List<Long> optionIds;   // 선택적, null 또는 빈 리스트 가능

}
