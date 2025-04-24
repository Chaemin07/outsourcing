package com.example.outsourcing.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CartItemDto {
    private Long cartId;
    private Long menuId;
    private String menuName;
    private Integer quantity;
    private Integer menuPrice;
    private Integer subTotalPrice; // 메뉴 항목별 총 금액(메뉴가격*수량+옵션가격)

    private List<OptionDto> options;
}
