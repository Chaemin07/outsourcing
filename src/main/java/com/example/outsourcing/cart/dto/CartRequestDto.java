package com.example.outsourcing.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CartRequestDto {
    @NotNull(message = "가게 ID는 필수입니다.")
    private Long storeId;
    @NotNull(message = "메뉴 ID는 필수입니다.")
    private Long menuId;
    @NotNull(message = "수량은 필수입니다.")
    @Positive(message = "수량은 1 이상이어야 합니다.")
    private Integer quantity;
    private List<Long> optionIds;   // 선택적, null 또는 빈 리스트 가능

}
