package com.example.outsourcing.favorite.controller;

import com.example.outsourcing.common.annotation.AuthUser;
import com.example.outsourcing.common.response.ApiResponse;
import com.example.outsourcing.favorite.dto.reponse.FavoriteResponseDto;
import com.example.outsourcing.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores/{storeId}/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    // 즐겨찾기 추가
    @PostMapping
    public ResponseEntity<ApiResponse<FavoriteResponseDto>> favoriteStore(@PathVariable Long storeId, @AuthUser Long userId) {

        FavoriteResponseDto favoriteResponseDto = favoriteService.favoriteStore(userId, storeId);

        return ResponseEntity.ok(ApiResponse.success("즐겨찾기 추가가 완료되었습니다.", favoriteResponseDto));
    }

    // 즐겨찾기 삭제
    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> deleteFavorite(@PathVariable Long storeId, @AuthUser Long userId) {

        favoriteService.deleteFavorite(userId, storeId);

        return ResponseEntity.ok(ApiResponse.success("즐겨찾기 추가가 완료되었습니다."));
    }
}
