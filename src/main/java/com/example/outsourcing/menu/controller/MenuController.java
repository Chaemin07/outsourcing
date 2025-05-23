package com.example.outsourcing.menu.controller;

import com.example.outsourcing.common.response.ApiResponse;
import com.example.outsourcing.image.util.ImageUtil;
import com.example.outsourcing.menu.dto.request.AddMenuRequestDto;
import com.example.outsourcing.menu.dto.request.UpdateMenuRequestDto;
import com.example.outsourcing.menu.dto.response.MenuResponseDto;
import com.example.outsourcing.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores/{storeId}/menus")
public class MenuController {

  private final MenuService menuService;
  private final ImageUtil imageUtil;

  @PostMapping
  public ResponseEntity<ApiResponse<MenuResponseDto>> addMenu(
      @PathVariable Long storeId,
      @Valid @RequestBody AddMenuRequestDto requestDto
  ) {
    MenuResponseDto menuResponseDto = menuService.addMenu(storeId, requestDto);

    return ResponseEntity.ok(ApiResponse.success(menuResponseDto));

  }

  // 메뉴 이미지 업로드
  @PostMapping("/{menuId}/img")
  public ResponseEntity<Void> uploadImage(
      @PathVariable Long menuId, @RequestParam MultipartFile image) {
    menuService.uploadMenuImg(menuId, image);
    return ResponseEntity.ok().build();
  }

  // 메뉴 이미지 조회
  @GetMapping("/{menuId}/img")
  public ResponseEntity<Resource> getImage(@PathVariable Long menuId) {
    return imageUtil.getImage(menuService.getMenuImgId(menuId));
  }

  @GetMapping("/{menuId}")
  public ResponseEntity<ApiResponse<MenuResponseDto>> getMenuById(
      @PathVariable Long storeId,
      @PathVariable Long menuId
  ) {
    MenuResponseDto menuById = menuService.getMenuById(storeId, menuId);

    return ResponseEntity.ok(ApiResponse.success(menuById));

  }

  @PutMapping("/{menuId}")
  public ResponseEntity<ApiResponse<MenuResponseDto>> updateMenu(
      @PathVariable Long storeId,
      @PathVariable Long menuId,
      @Valid @RequestBody UpdateMenuRequestDto requestDto
  ) {
    MenuResponseDto menuResponseDto = menuService.updateMenu(storeId, menuId, requestDto);

    return ResponseEntity.ok(ApiResponse.success(menuResponseDto));
  }

  @DeleteMapping("/{menuId}")
  public ResponseEntity<ApiResponse<Void>> deleteMenu(
      @PathVariable Long storeId,
      @PathVariable Long menuId
  ) {
    menuService.deleteMenu(storeId, menuId);

    return ResponseEntity.ok(ApiResponse.success("삭제되었습니다."));

  }

}
