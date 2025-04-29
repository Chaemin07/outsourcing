package com.example.outsourcing.store.controller;

import com.example.outsourcing.common.annotation.AuthUser;
import com.example.outsourcing.common.response.ApiResponse;
import com.example.outsourcing.image.util.ImageUtil;
import com.example.outsourcing.menu.dto.response.MenuSummaryResponseDto;
import com.example.outsourcing.store.dto.request.CreateStoreRequestDto;
import com.example.outsourcing.store.dto.request.UpdateStoreRequestDto;
import com.example.outsourcing.store.dto.response.CreateStoreResponseDto;
import com.example.outsourcing.store.dto.response.GetStoreWithMenuResponseDto;
import com.example.outsourcing.store.dto.response.StoreResponseDto;
import com.example.outsourcing.store.dto.response.UpdateStoreResponseDto;
import com.example.outsourcing.store.service.StoreService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ApiResponse<CreateStoreResponseDto>> createStore(
        @Valid @RequestBody CreateStoreRequestDto requestDto,
        @AuthUser Long userId) {

        CreateStoreResponseDto responseDto = storeService.createStore(requestDto, userId);

        return ResponseEntity.ok(ApiResponse.success(responseDto));
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
    public ResponseEntity<ApiResponse<List<StoreResponseDto>>> getStore() {
        List<StoreResponseDto> storeList = storeService.getStore();
        return ResponseEntity.ok(ApiResponse.success(storeList));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<StoreResponseDto>>> searchStores(@RequestParam String keyword) {
        List<StoreResponseDto> storeList = storeService.searchStores(keyword);
        return ResponseEntity.ok(ApiResponse.success(storeList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetStoreWithMenuResponseDto>> getStoreById(@PathVariable Long id) {
        GetStoreWithMenuResponseDto responseDto = storeService.getStoreById(id);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UpdateStoreResponseDto>> updateStore(
        @PathVariable Long id,
        @AuthUser Long userId,
        @Valid @RequestBody UpdateStoreRequestDto requestDto) {

        UpdateStoreResponseDto responseDto = storeService.updateStore(id, requestDto, userId);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> closedDownStore(@PathVariable Long id, @AuthUser Long userId) {
        storeService.closedDownStore(id, userId);
        return ResponseEntity.ok(ApiResponse.success());
    }

}
