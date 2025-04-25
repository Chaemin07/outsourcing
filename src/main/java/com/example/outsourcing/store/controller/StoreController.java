package com.example.outsourcing.store.controller;

import com.example.outsourcing.common.annotation.AuthUser;
import com.example.outsourcing.store.dto.request.CreateStoreRequestDto;
import com.example.outsourcing.store.dto.request.UpdateStoreRequestDto;
import com.example.outsourcing.store.dto.response.CreateStoreResponseDto;
import com.example.outsourcing.store.dto.response.GetStoreWithMenuResponseDto;
import com.example.outsourcing.store.dto.response.StoreResponseDto;
import com.example.outsourcing.store.service.StoreService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<CreateStoreResponseDto> createStore(
        @Valid @RequestBody CreateStoreRequestDto requestDto,
        @AuthUser Long userId) {

        CreateStoreResponseDto responseDto = storeService.createStore(requestDto, userId);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<StoreResponseDto>> getStore() {
        List<StoreResponseDto> storeList = storeService.getStore();
        return ResponseEntity.ok(storeList);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StoreResponseDto>> searchStores(@RequestParam String keyword) {
        List<StoreResponseDto> storeList = storeService.searchStores(keyword);
        return ResponseEntity.ok(storeList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetStoreWithMenuResponseDto> getStoreById(@PathVariable Long id) {
        GetStoreWithMenuResponseDto responseDto = storeService.getStoreById(id);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreResponseDto> updateStore(
        @PathVariable Long id,
        @AuthUser Long userId,
        @Valid @RequestBody UpdateStoreRequestDto requestDto) {

        StoreResponseDto responseDto = storeService.updateStore(id, requestDto, userId);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> closedDownStore(@PathVariable Long id, @AuthUser Long userId) {
        storeService.closedDownStore(id);
        return ResponseEntity.noContent().build();
    }
}
