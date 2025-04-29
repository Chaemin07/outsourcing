package com.example.outsourcing.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class CartItemDto {
    @NotNull
    private Long cartId;
    @NotNull
    private Long menuId;
    private String menuName;
    @Positive
    private Integer quantity;
    @Positive
    private Integer menuPrice;
    private Integer subTotalPrice; // 메뉴 항목별 총 금액(메뉴가격*수량+옵션가격)
    @Size(min = 0)
    private List<OptionDto> options;
}
