package com.example.outsourcing.category.dto.response;

import com.example.outsourcing.category.entity.Category;
import com.example.outsourcing.common.enums.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponseDto {

    private final Long id;

    private final CategoryType type;

    private final Long storeId;

    public CategoryResponseDto(Category category) {
        this.id = category.getId();
        this.type = category.getType();
        this.storeId = category.getStore().getId();
    }
}
