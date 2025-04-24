package com.example.outsourcing.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OptionDto {
    private Long optionId;
    private String optionType;
    private String content;
    private Integer optionPrice;
}
