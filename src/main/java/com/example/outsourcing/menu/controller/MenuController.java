package com.example.outsourcing.menu.controller;

import com.example.outsourcing.menu.dto.request.AddMenuRequestDto;
import com.example.outsourcing.menu.dto.response.MenuResponseDto;
import com.example.outsourcing.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores/{storeId}/menus")
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<MenuResponseDto> addMenu(
        @PathVariable Long storeId,
        @RequestBody AddMenuRequestDto requestDto
    ) {

        MenuResponseDto menuResponseDto = menuService.addMenu(storeId, requestDto);

        return ResponseEntity.ok(menuResponseDto);

    }

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuResponseDto> getMenuById(
        @PathVariable Long storeId,
        @PathVariable Long menuId
        ) {

        MenuResponseDto menuById = menuService.getMenuById(storeId, menuId);

        return ResponseEntity.ok(menuById);

    }

}
