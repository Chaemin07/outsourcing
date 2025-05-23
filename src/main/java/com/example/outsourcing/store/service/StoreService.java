package com.example.outsourcing.store.service;

import com.example.outsourcing.address.entity.Address;
import com.example.outsourcing.common.exception.BaseException;
import com.example.outsourcing.common.exception.ErrorCode;
import com.example.outsourcing.image.service.ImageService;
import com.example.outsourcing.menu.dto.response.MenuSummaryResponseDto;
import com.example.outsourcing.menu.entity.Menu;
import com.example.outsourcing.menu.repository.MenuRepository;
import com.example.outsourcing.store.dto.request.CreateStoreRequestDto;
import com.example.outsourcing.store.dto.request.UpdateStoreRequestDto;
import com.example.outsourcing.store.dto.response.CreateStoreResponseDto;
import com.example.outsourcing.store.dto.response.GetStoreWithMenuResponseDto;
import com.example.outsourcing.store.dto.response.StoreResponseDto;
import com.example.outsourcing.store.dto.response.UpdateStoreResponseDto;
import com.example.outsourcing.store.entity.Store;
import com.example.outsourcing.store.entity.StoreStatus;
import com.example.outsourcing.store.repository.StoreRepository;
import com.example.outsourcing.user.entity.Role;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

    @Transactional
    public CreateStoreResponseDto createStore(CreateStoreRequestDto requestDto, Long userId) {

        // 사용자(사장) 검증
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER_ID));

        // User_Role 검증
        if (!user.getRole().equals(Role.OWNER)) {
            throw new BaseException(ErrorCode.FORBIDDEN_STORE);
        }

        // 사장님 점포 수 검증 3개 이하 인지
        int storeCount = storeRepository.countByUserIdAndStatusNot(userId, StoreStatus.CLOSED_DOWN);
        if (storeCount >= 3) {
            throw new BaseException(ErrorCode.EXCEED_STORE_LIMIT);
        }

        // 가게 저장
        Store savedStore = storeRepository.save(new Store(requestDto, user));

        Address address = new Address(requestDto.getAddress(), savedStore);

        savedStore.setAddress(address);

        return CreateStoreResponseDto.toDto(savedStore);
    }

    public List<StoreResponseDto> getStore() {
        return storeRepository.findAllByStatusNot(StoreStatus.CLOSED_DOWN)
            .stream()
            .map(StoreResponseDto::toDto)
            .toList();
    }

    @Transactional
    public List<StoreResponseDto> searchStores(String keyword) {

        return storeRepository.findByNameContainingAndStatusNot(keyword, StoreStatus.CLOSED_DOWN)
            .stream()
            .map(StoreResponseDto::toDto)
            .toList();
    }

    @Transactional
    public GetStoreWithMenuResponseDto getStoreById(Long id) {
        Store findStore = storeRepository.findByIdOrElseThrow(id);

        List<Menu> menus = menuRepository.findByStoreId(id);

        List<MenuSummaryResponseDto> menuList = menus.stream()
            .map(MenuSummaryResponseDto::toDto)
            .collect(Collectors.toList());

        return GetStoreWithMenuResponseDto.toDto(findStore, menuList);
    }

    @Transactional
    public UpdateStoreResponseDto updateStore(Long id, UpdateStoreRequestDto requestDto, Long userId) {
        Store findStore = storeRepository.findByIdOrElseThrow(id);

        if (!findStore.getUser().getId().equals(userId)) {
            throw new BaseException(ErrorCode.FORBIDDEN_STORE);
        }

        findStore.updateStore(requestDto, new Address(requestDto.getAddress(), findStore));

        return UpdateStoreResponseDto.toDto(findStore);
    }

    @Transactional
    public void closedDownStore(Long id, Long userId) {
        Store findStore = storeRepository.findByIdOrElseThrow(id);

        // 가게 주인 검증
        if (!findStore.getUser().getId().equals(userId)) {
            throw new BaseException(ErrorCode.FORBIDDEN_STORE);
        }

        findStore.closeDown();
    }

    // 가게 이미지 업로드
    @Transactional(rollbackFor = RuntimeException.class)
    public void uploadStoreImg(Long id, MultipartFile file) {
        Store findStore = storeRepository.findByIdOrElseThrow(id);

    try {
      findStore.setImage(imageService.uploadImage(file));   // 업로드 후 가게 이미지에 값 설정
    } catch (RuntimeException e) {
      new BaseException(ErrorCode.IMAGE_UPLOAD_FAILED);
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
