package com.example.outsourcing.menu.service;

import com.example.outsourcing.menu.dto.request.AddMenuRequestDto;
import com.example.outsourcing.menu.dto.request.UpdateMenuRequestDto;
import com.example.outsourcing.menu.dto.response.MenuResponseDto;
import com.example.outsourcing.menu.entity.Menu;
import com.example.outsourcing.menu.repository.MenuRepository;
import com.example.outsourcing.store.entity.Store;
import com.example.outsourcing.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public MenuResponseDto addMenu (Long storeId, AddMenuRequestDto requestDto) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        Menu menu = menuRepository.save(new Menu(store, requestDto));

        return MenuResponseDto.toDto(menu);
    }

    @Transactional
    public MenuResponseDto getMenuById(Long storeId, Long menuId) {

        Menu getMenu = menuRepository.findByIdOrElseThrow(menuId);

        getMenu.validateStore(storeId);

        return MenuResponseDto.toDto(getMenu);
    }

    @Transactional
    public MenuResponseDto updateMenu(Long storeId, Long menuId,
        UpdateMenuRequestDto requestDto) {

        Menu findMenu = menuRepository.findByIdOrElseThrow(menuId);

        findMenu.validateStore(storeId);

        findMenu.updateMenu(requestDto);

        return MenuResponseDto.toDto(findMenu);
    }

    @Transactional
    public void deleteMenu(Long storeId, Long menuId) {

        Menu menu = menuRepository.findByIdOrElseThrow(menuId);

        menu.validateStore(storeId);

        menu.softDelete();
    }
}
