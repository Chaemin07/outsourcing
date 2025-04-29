package com.example.outsourcing.category.dto.request;

import com.example.outsourcing.common.enums.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryRequestDto {

    private final CategoryType type;
}
