package com.example.outsourcing.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OptionDto {
    private Long optionId;
    private String optionName;
    private Integer optionPrice;
}
