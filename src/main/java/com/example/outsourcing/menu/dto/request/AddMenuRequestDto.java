package com.example.outsourcing.menu.dto.request;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AddMenuRequestDto {

    private final String name;

    private final Integer price;

    private final String descrption;

}
