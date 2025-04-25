package com.example.outsourcing.menu.dto.response;

import com.example.outsourcing.menu.entity.Menu;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MenuResponseDto {

    private final Long id;

    private final String name;

    private final Integer price;

    private final String descrption;

    private final String status;

    private final Long storeId;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    public static MenuResponseDto toDto(Menu menu) {
        return new MenuResponseDto(
            menu.getId(),
            menu.getName(),
            menu.getPrice(),
            menu.getDescrption(),
            menu.getStatus(),
            menu.getStore().getId(),
            menu.getCreatedAt(),
            menu.getUpdatedAt()
        );
    }

}
