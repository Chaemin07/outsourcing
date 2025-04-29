package com.example.outsourcing.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OptionDto {
    private Long optionId;
    @NotNull
    private String optionName;
    @NotNull
    @PositiveOrZero
    private Integer optionPrice;
}
