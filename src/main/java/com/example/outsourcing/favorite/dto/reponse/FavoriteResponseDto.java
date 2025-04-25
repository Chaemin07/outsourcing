package com.example.outsourcing.favorite.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteResponseDto {

    private final Long userId;

    private final Long storeId;

    private final String message;
}
