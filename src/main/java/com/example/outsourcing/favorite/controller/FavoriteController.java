package com.example.outsourcing.favorite.controller;

import com.example.outsourcing.common.annotation.AuthUser;
import com.example.outsourcing.favorite.dto.reponse.FavoriteResponseDto;
import com.example.outsourcing.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores/{storeId}/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    // 즐겨찾기 추가
    @PostMapping
    public ResponseEntity<FavoriteResponseDto> favoriteStore(@PathVariable Long storeId, @AuthUser Long userId) {

        FavoriteResponseDto favoriteResponseDto = favoriteService.favoriteStore(userId, storeId);

        return new ResponseEntity<>(favoriteResponseDto, HttpStatus.OK);
    }

    // 즐겨찾기 삭제
    @DeleteMapping
    public ResponseEntity<String> deleteFavorite(@PathVariable Long storeId, @AuthUser Long userId) {

        favoriteService.deleteFavorite(userId, storeId);

        return new ResponseEntity<>("즐겨찾기 삭제가 완료되었습니다.", HttpStatus.OK);
    }
}
