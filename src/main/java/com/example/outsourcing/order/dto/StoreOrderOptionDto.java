package com.example.outsourcing.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreOrderOptionDto {
    private String optionName;
    private Integer optionPrice;
}
