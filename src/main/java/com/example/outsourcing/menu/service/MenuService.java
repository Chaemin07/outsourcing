package com.example.outsourcing.menu.service;

import com.example.outsourcing.common.exception.BaseException;
import com.example.outsourcing.common.exception.ErrorCode;
import com.example.outsourcing.image.service.ImageService;
import com.example.outsourcing.menu.dto.request.AddMenuRequestDto;
import com.example.outsourcing.menu.dto.request.UpdateMenuRequestDto;
import com.example.outsourcing.menu.dto.response.MenuResponseDto;
import com.example.outsourcing.menu.entity.Menu;
import com.example.outsourcing.menu.repository.MenuRepository;
import com.example.outsourcing.store.entity.Store;
import com.example.outsourcing.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MenuService {

  private final MenuRepository menuRepository;
  private final StoreRepository storeRepository;
  private final ImageService imageService;

  @Transactional
  public MenuResponseDto addMenu(Long storeId, AddMenuRequestDto requestDto) {

    Store store = storeRepository.findByIdOrElseThrow(storeId);

    Menu menu = menuRepository.save(new Menu(store, requestDto));

    return MenuResponseDto.toDto(menu);
  }

  @Transactional
  public MenuResponseDto getMenuById(Long storeId, Long menuId) {

    Menu menu = menuRepository.findByIdOrElseThrow(menuId);

    // 우리 가게 메뉴인지 검증
    if(!menu.getStore().getId().equals(storeId)){
      throw new BaseException(ErrorCode.INVALID_MENU_ID);
    }

    // 삭제된 메뉴 검증
    if(menu.isDeleted()) {
      throw new BaseException(ErrorCode.INVALID_MENU_ID);
    }

    return MenuResponseDto.toDto(menu);
  }

  @Transactional
  public MenuResponseDto updateMenu(Long storeId, Long menuId,
      UpdateMenuRequestDto requestDto) {

    Menu findMenu = menuRepository.findByIdOrElseThrow(menuId);
    // 가게 본인 소유 검증
    findMenu.validateStore(storeId);

    // 삭제 메뉴 수정 불가
    if (findMenu.isDeleted()) {
      throw new BaseException(ErrorCode.MENU_ALREADY_DELETED);
    }

    findMenu.updateMenu(requestDto);

    return MenuResponseDto.toDto(findMenu);
  }

  @Transactional
  public void deleteMenu(Long storeId, Long menuId) {

    Menu menu = menuRepository.findByIdOrElseThrow(menuId);

    // 가게 본인 소유 검증
    menu.validateStore(storeId);

    menu.softDelete();
  }

  // 메뉴 이미지 업로드
  @Transactional(rollbackFor = RuntimeException.class)
  public void uploadMenuImg(Long menuId, MultipartFile file) {
    Menu findMenu = menuRepository.findByIdOrElseThrow(menuId);

    try {
      findMenu.setImage(imageService.uploadImage(file));   // 업로드 후 메뉴 이미지에 값 설정
    } catch (RuntimeException e) {
      throw new BaseException(ErrorCode.IMAGE_UPLOAD_FAILED);
    }
  }

  // 메뉴 이미지 조회
  public Long getMenuImgId(Long menuId) {
    Menu findMenu = menuRepository.findByIdOrElseThrow(menuId);
      if (findMenu.getImage() != null) {
          return findMenu.getImage().getId();
      } else {
          return null;
      }
  }
}
