package com.example.outsourcing.store.service;

import com.example.outsourcing.image.service.ImageService;
import com.example.outsourcing.store.dto.request.CreateStoreRequestDto;
import com.example.outsourcing.store.dto.request.UpdateStoreRequestDto;
import com.example.outsourcing.store.dto.response.CreateStoreResponseDto;
import com.example.outsourcing.store.dto.response.StoreResponseDto;
import com.example.outsourcing.store.entity.Store;
import com.example.outsourcing.store.repository.StoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;
  private final ImageService imageService;

  @Transactional
  public CreateStoreResponseDto createStore(CreateStoreRequestDto requestDto) {

    Store savedStore = storeRepository.save(new Store(requestDto));

    return CreateStoreResponseDto.toDto(savedStore);
  }

  @Transactional
  public List<StoreResponseDto> getStore() {
    return storeRepository.findAll()
        .stream()
        .map(StoreResponseDto::toDto)
        .toList();
  }

  @Transactional
  public StoreResponseDto getStoreById(Long id) {

    Store findStore = storeRepository.findByIdOrElseThrow(id);

    return StoreResponseDto.toDto(findStore);
  }

  @Transactional
  public StoreResponseDto updateStore(Long id, UpdateStoreRequestDto requestDto) {

    Store findStore = storeRepository.findByIdOrElseThrow(id);

    findStore.updateStore(requestDto);

    return StoreResponseDto.toDto(findStore);
  }

  @Transactional
  public void closedDownStore(Long id) {

    Store findStore = storeRepository.findByIdOrElseThrow(id);

    storeRepository.delete(findStore);

  }

  // 가게 이미지 업로드
  @Transactional(rollbackFor = RuntimeException.class)
  public void uploadStoreImg(Long id, MultipartFile file) {
    Store findStore = storeRepository.findByIdOrElseThrow(id);

    try {
      findStore.setImage(imageService.uploadImage(file));   // 업로드 후 가게 이미지에 값 설정
    } catch (RuntimeException e) {
      new RuntimeException("파일 업로드에 실패하였습니다.", e);
    }
  }

  // 가게 이미지 조회
  public Long getStoreImgId(Long id) {
    Store findStore = storeRepository.findByIdOrElseThrow(id);
    if (findStore.getImage() != null) {
      return findStore.getImage().getId();
    } else {
      return null;
    }
  }
}
