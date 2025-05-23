package com.example.outsourcing.menu.dto.response;

import com.example.outsourcing.menu.entity.Menu;
import com.example.outsourcing.store.entity.Store;
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

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    public static MenuResponseDto toDto(Menu menu) {
        return new MenuResponseDto(
            menu.getId(),
            menu.getName(),
            menu.getPrice(),
            menu.getDescrption(),
            menu.getStatus().name(),
            menu.getCreatedAt(),
            menu.getUpdatedAt()
        );
    }

}
