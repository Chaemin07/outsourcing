package com.example.outsourcing.store.service;

import com.example.outsourcing.address.entity.Address;
import com.example.outsourcing.image.entity.Image;
import com.example.outsourcing.menu.dto.response.AllMenuResponseDto;
import com.example.outsourcing.menu.entity.Menu;
import com.example.outsourcing.menu.repository.MenuRepository;
import com.example.outsourcing.store.dto.request.CreateStoreRequestDto;
import com.example.outsourcing.store.dto.request.UpdateStoreRequestDto;
import com.example.outsourcing.store.dto.response.CreateStoreResponseDto;
import com.example.outsourcing.store.dto.response.GetStoreWithMenuResponseDto;
import com.example.outsourcing.store.dto.response.StoreResponseDto;
import com.example.outsourcing.store.entity.Store;
import com.example.outsourcing.store.repository.StoreRepository;
import com.example.outsourcing.user.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public CreateStoreResponseDto createStore(CreateStoreRequestDto requestDto) {

        Store savedStore = storeRepository.save(new Store(requestDto));

        return CreateStoreResponseDto.toDto(savedStore);
    }

    @Transactional
    public List<StoreResponseDto> getStore() {
        return storeRepository.findAllByDeletedAtIsNull()
            .stream()
            .map(StoreResponseDto::toDto)
            .toList();
    }

    @Transactional
    public GetStoreWithMenuResponseDto getStoreById(Long id) {

        Store findStore = storeRepository.findByIdOrElseThrow(id);
        // storeId로 해당 가게 menu 조회
        List<Menu> menus = menuRepository.findByStoreId(id);

        List<AllMenuResponseDto> menuList = menus.stream()
            .map(AllMenuResponseDto::toDto)
            .collect(Collectors.toList());

        return GetStoreWithMenuResponseDto.toDto(findStore, menuList);
    }

    @Transactional
    public StoreResponseDto updateStore(Long id, UpdateStoreRequestDto requestDto) {

        Store findStore = storeRepository.findByIdOrElseThrow(id);

        findStore.updateStore(requestDto);

        return StoreResponseDto.toDto(findStore);
    }

    @Transactional
    public void closedDownStore(Long id) {

        Store fingStore = storeRepository.findByIdOrElseThrow(id);

        fingStore.softDelete();

    }
}
