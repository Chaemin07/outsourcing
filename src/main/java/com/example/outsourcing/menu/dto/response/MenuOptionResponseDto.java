package com.example.outsourcing.menu.dto.response;

import com.example.outsourcing.menu.entity.MenuOption;
import com.example.outsourcing.menu.entity.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MenuOptionResponseDto {

    private final Long id;

    private final String optionName;

    private final Integer price;

    private final Status status;

    public static MenuOptionResponseDto of(MenuOption menuOption) {
        return new MenuOptionResponseDto(
            menuOption.getId(),
            menuOption.getOptionName(),
            menuOption.getPrice(),
            menuOption.getStatus()
        );
    }

    public static MenuOptionResponseDto toDto(MenuOption menuOption) {
        return new MenuOptionResponseDto(
            menuOption.getId(),
            menuOption.getOptionName(),
            menuOption.getPrice(),
            menuOption.getStatus()
        );
    }
}
