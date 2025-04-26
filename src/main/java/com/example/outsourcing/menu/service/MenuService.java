package com.example.outsourcing.menu.service;

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
      throw new RuntimeException("저희 가게 음식이 아닙니다.");
    }

    // 삭제된 메뉴 검증
    if(menu.isDeleted()) {
      throw new RuntimeException("더 이상 이 메뉴는 만들지 않습니다.");
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
      throw new RuntimeException("삭제된 메뉴는 수정할 수 없습니다.");
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
      throw new RuntimeException("파일 업로드에 실패하였습니다.", e);
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
