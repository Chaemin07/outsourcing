package com.example.outsourcing.menu.service;

import com.example.outsourcing.menu.dto.request.AddMenuOptionRequestDto;
import com.example.outsourcing.menu.dto.response.MenuOptionResponseDto;
import com.example.outsourcing.menu.entity.Menu;
import com.example.outsourcing.menu.entity.MenuOption;
import com.example.outsourcing.menu.repository.MenuOptionRepository;
import com.example.outsourcing.menu.repository.MenuRepository;
import com.example.outsourcing.store.dto.request.UpdateMenuOptionRequestDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MenuOptionService {

    private final MenuOptionRepository menuOptionRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public MenuOptionResponseDto addMenuOption(Long menuId, AddMenuOptionRequestDto requestDto) {

        // 메뉴 존재 확인
        Menu menu = menuRepository.findByIdOrElseThrow(menuId);

        // 저장
        MenuOption menuOption = menuOptionRepository.save(new MenuOption(menu, requestDto));

        // 응답
        return MenuOptionResponseDto.of(menuOption);
    }

    public List<MenuOptionResponseDto> getMenuOptions(Long menuId) {

        List<MenuOption> menuOptions = menuOptionRepository.findAllByMenuIdAndDeletedAtIsNull(menuId);

        return menuOptions.stream()
            .map(MenuOptionResponseDto::toDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public MenuOptionResponseDto updateMenuOption(Long menuOptionId, UpdateMenuOptionRequestDto requestDto) {
        MenuOption menuOption = menuOptionRepository.findByIdAndDeletedAtIsNull(menuOptionId);

        if (menuOption == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "메뉴 옵션을 찾을 수 없습니다.");
        }

        if (requestDto.getOptionName() != null || requestDto.getPrice() != null) {
            menuOption.updateOption(requestDto.getOptionName(), requestDto.getPrice());
        }

        return MenuOptionResponseDto.of(menuOption);
    }

    @Transactional
    public void deleteMenuOption(Long menuOptionId) {
        MenuOption menuOption = menuOptionRepository.findByIdAndDeletedAtIsNull(menuOptionId);

        if (menuOption == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제할 수 있는 메뉴 옵션이 없습니다.");
        }

        menuOption.softDelete();
    }
}
