package com.example.outsourcing.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StoreOrderDetailDto {
    private String menuName;
    private Integer menuPrice;
    private Integer quantity;
}