package com.example.outsourcing.menu.controller;

import com.example.outsourcing.menu.dto.request.AddMenuOptionRequestDto;
import com.example.outsourcing.menu.dto.response.MenuOptionResponseDto;
import com.example.outsourcing.menu.service.MenuOptionService;
import com.example.outsourcing.store.dto.request.UpdateMenuOptionRequestDto;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus/{menuId}/options")
public class MenuOptionController {

    private final MenuOptionService menuOptionService;

    @PostMapping
    public ResponseEntity<MenuOptionResponseDto> addMenuOption(
        @PathVariable Long menuId,
        @Valid @RequestBody AddMenuOptionRequestDto requestDto
    ) {
        MenuOptionResponseDto responseDto = menuOptionService.addMenuOption(menuId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<MenuOptionResponseDto>> getMenuOptions(
        @PathVariable Long menuId
    ) {
        List<MenuOptionResponseDto> responseDtoList = menuOptionService.getMenuOptions(menuId);
        return ResponseEntity.ok(responseDtoList);
    }

    @PatchMapping("/{optionId}")
    public ResponseEntity<MenuOptionResponseDto> updateMenuOption(
        @PathVariable Long optionId,
        @Valid @RequestBody UpdateMenuOptionRequestDto requestDto
    ) {
        MenuOptionResponseDto responseDto = menuOptionService.updateMenuOption(optionId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> deleteMenuOption(
        @PathVariable Long menuId,
        @PathVariable Long optionId
    ) {
        menuOptionService.deleteMenuOption(optionId);
        return ResponseEntity.ok().build();
    }
}
