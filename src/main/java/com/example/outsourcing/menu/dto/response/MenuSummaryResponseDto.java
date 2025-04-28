package com.example.outsourcing.menu.dto.response;


import com.example.outsourcing.menu.entity.Menu;
import com.example.outsourcing.store.entity.Store;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MenuSummaryResponseDto {

    private final Long id;

    private final String name;

    private final Integer price;

    private final String descrption;

    private final String status;

    public static MenuSummaryResponseDto toDto(Menu menu) {
        return new MenuSummaryResponseDto(
            menu.getId(),
            menu.getName(),
            menu.getPrice(),
            menu.getDescrption(),
            menu.getStatus().name()
        );
    }

}
