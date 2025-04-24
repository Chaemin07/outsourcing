package com.example.outsourcing.store.controller;

import com.example.outsourcing.store.dto.request.CreateStoreRequestDto;
import com.example.outsourcing.store.dto.request.UpdateStoreRequestDto;
import com.example.outsourcing.store.dto.response.CreateStoreResponseDto;
import com.example.outsourcing.store.dto.response.StoreResponseDto;
import com.example.outsourcing.store.service.StoreService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<CreateStoreResponseDto> createStore(
        @RequestBody CreateStoreRequestDto requestDto) {

        CreateStoreResponseDto createStoreResponseDto =
            storeService.createStore(
                requestDto.getName(),
                requestDto.getStatus(),
                requestDto.getStorePhoneNumber(),
                requestDto.getMinOderPrice(),
                requestDto.getOpeningTimes(),
                requestDto.getClosingTimes(),
                requestDto.getNotification()
            );

        return ResponseEntity.ok(createStoreResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<StoreResponseDto>> findAll() {
        List<StoreResponseDto> storeResponseDtoList = storeService.findAll();

        return ResponseEntity.ok(storeResponseDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreResponseDto> findById(@PathVariable Long id) {

        StoreResponseDto storeResponseDto = storeService.findById(id);

        return ResponseEntity.ok(storeResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreResponseDto> updateStore(@PathVariable Long id, @RequestBody
        UpdateStoreRequestDto requestDto) {

        StoreResponseDto storeResponseDto = storeService.updateStore(
            id,
            requestDto.getName(),
            requestDto.getStatus(),
            requestDto.getStorePhoneNumber(),
            requestDto.getMinOderPrice(),
            requestDto.getOpeningTimes(),
            requestDto.getClosingTimes(),
            requestDto.getNotification()
        );

        return ResponseEntity.ok(storeResponseDto);
    }

}
