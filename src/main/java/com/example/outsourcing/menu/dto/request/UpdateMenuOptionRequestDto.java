package com.example.outsourcing.menu.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateMenuOptionRequestDto {

    private final String optionName;

    private final Integer price;

}
