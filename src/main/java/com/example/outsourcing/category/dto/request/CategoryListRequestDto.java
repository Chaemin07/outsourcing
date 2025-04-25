package com.example.outsourcing.category.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CategoryListRequestDto {

    private final List<CategoryRequestDto> typeList;
}
