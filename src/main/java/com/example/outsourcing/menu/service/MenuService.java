package com.example.outsourcing.menu.service;

import com.example.outsourcing.menu.dto.request.AddMenuRequestDto;
import com.example.outsourcing.menu.dto.response.MenuResponseDto;
import com.example.outsourcing.menu.entity.Menu;
import com.example.outsourcing.menu.repository.MenuRepository;
import com.example.outsourcing.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public MenuResponseDto addMenu (Long storeId, AddMenuRequestDto requestDto) {

        storeRepository.findByIdOrElseThrow(storeId);

        Menu menu = menuRepository.save(new Menu(requestDto));

        return MenuResponseDto.toDto(menu);
    }

    @Transactional
    public MenuResponseDto getMenuById(Long storeId, Long menuId) {

        storeRepository.findByIdOrElseThrow(storeId);

        Menu getMenu = menuRepository.findByIdOrElseThrow(menuId);

        return MenuResponseDto.toDto(getMenu);
    }
}
