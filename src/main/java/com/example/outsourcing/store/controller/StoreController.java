package com.example.outsourcing.store.controller;

import com.example.outsourcing.image.util.ImageUtil;
import com.example.outsourcing.store.dto.request.CreateStoreRequestDto;
import com.example.outsourcing.store.dto.request.UpdateStoreRequestDto;
import com.example.outsourcing.store.dto.response.CreateStoreResponseDto;
import com.example.outsourcing.store.dto.response.StoreResponseDto;
import com.example.outsourcing.store.service.StoreService;
import java.util.List;
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
@RequestMapping("/stores")
public class StoreController {

  private final StoreService storeService;
  private final ImageUtil imageUtil;

  @PostMapping
  public ResponseEntity<CreateStoreResponseDto> createStore(
      @RequestBody CreateStoreRequestDto requestDto) {

    CreateStoreResponseDto createStoreResponseDto =
        storeService.createStore(requestDto);

    return ResponseEntity.ok(createStoreResponseDto);
  }

  // 가게 이미지 업로드
  @PostMapping("/{id}/img")
  public ResponseEntity<Void> uploadImage(
      @PathVariable Long id, @RequestParam MultipartFile image) {
    storeService.uploadStoreImg(id, image);
    return ResponseEntity.ok().build();
  }

  // 가게 이미지 조회
  @GetMapping("/{id}/img")
  public ResponseEntity<Resource> getImage(@PathVariable Long menuId) {
    return imageUtil.getImage(storeService.getStoreImgId(menuId));
  }

  @GetMapping
  public ResponseEntity<List<StoreResponseDto>> getStore() {

    List<StoreResponseDto> storeResponseDtoList = storeService.getStore();

    return ResponseEntity.ok(storeResponseDtoList);
  }

  @GetMapping("/{id}")
  public ResponseEntity<StoreResponseDto> getStoreById(@PathVariable Long id) {

    StoreResponseDto storeResponseDto = storeService.getStoreById(id);

    return ResponseEntity.ok(storeResponseDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<StoreResponseDto> updateStore(@PathVariable Long id, @RequestBody
  UpdateStoreRequestDto requestDto) {

    StoreResponseDto storeResponseDto = storeService.updateStore(id, requestDto);

    return ResponseEntity.ok(storeResponseDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> closedDownStore(@PathVariable Long id) {

    storeService.closedDownStore(id);

    String deleteMessage = "삭제되었습니다";

    return ResponseEntity.ok(deleteMessage);
  }

}
