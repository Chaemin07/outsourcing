package com.example.outsourcing.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderItemRequestDto {
    @NotNull(message = "메뉴 ID는 필수입니다.")
    private Long menuId;

    @NotNull(message = "수량은 필수입니다.")
    @Positive(message = "수량은 1 이상이어야 합니다.")
    private Integer quantity;
    private List<Long> optionIds;
}
