package com.example.outsourcing.menu.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateMenuRequestDto {

    private final String name;

    private final Integer price;

    private final String descrption;

    private final String status;
}
