package com.example.outsourcing.menu.dto.response;

import com.example.outsourcing.menu.entity.Menu;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AllMenuResponseDto {

    private final Long id;

    private final String name;

    private final Integer price;

    private final String descrption;

    private final String status;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    public static AllMenuResponseDto toDto(Menu menu) {
        return new AllMenuResponseDto(
            menu.getId(),
            menu.getName(),
            menu.getPrice(),
            menu.getDescrption(),
            menu.getStatus(),
            menu.getCreatedAt(),
            menu.getUpdatedAt()
        );
    }

}
