package com.example.outsourcing.store.service;

import com.example.outsourcing.address.entity.Address;
import com.example.outsourcing.image.entity.Image;
import com.example.outsourcing.store.dto.request.CreateStoreRequestDto;
import com.example.outsourcing.store.dto.request.UpdateStoreRequestDto;
import com.example.outsourcing.store.dto.response.CreateStoreResponseDto;
import com.example.outsourcing.store.dto.response.StoreResponseDto;
import com.example.outsourcing.store.entity.Store;
import com.example.outsourcing.store.repository.StoreRepository;
import com.example.outsourcing.user.entity.User;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

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

        Store fingStore = storeRepository.findByIdOrElseThrow(id);

        storeRepository.delete(fingStore);

    }
}
