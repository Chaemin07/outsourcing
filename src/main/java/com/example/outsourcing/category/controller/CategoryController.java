package com.example.outsourcing.category.controller;

import com.example.outsourcing.category.dto.request.CategoryListRequestDto;
import com.example.outsourcing.category.dto.request.CategoryRequestDto;
import com.example.outsourcing.category.dto.response.CategoryResponseDto;
import com.example.outsourcing.category.service.CategoryService;
import com.example.outsourcing.common.annotation.AuthUser;
import com.example.outsourcing.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores/{storeId}/categories")
public class CategoryController {

    private final CategoryService categoryService;

    // 카테고리 단일 추가
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponseDto>> saveCategory(@PathVariable Long storeId, @Valid @RequestBody CategoryRequestDto dto, @AuthUser Long userId) {

        CategoryResponseDto categoryResponseDto = categoryService.saveCategory(storeId, userId, dto);

        return ResponseEntity.ok(ApiResponse.success("카테고리 추가가 완료되었습니다.", categoryResponseDto));
    }

    // 카테고리 다중 추가
    @PostMapping("/lists")
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> saveCategoryList(@PathVariable Long storeId, @Valid @RequestBody CategoryListRequestDto dto, @AuthUser Long userId) {

        List<CategoryResponseDto> categoryResponseDto = categoryService.saveCategoryList(storeId, userId, dto);

        return ResponseEntity.ok(ApiResponse.success("카테고리 다중 추가가 완료되었습니다.", categoryResponseDto));
    }

    // 카테고리 단일 수정
    @PatchMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> updateCategory(@PathVariable Long storeId, @PathVariable Long categoryId, @Valid @RequestBody CategoryRequestDto dto, @AuthUser Long userId) {

        CategoryResponseDto categoryResponseDto = categoryService.updateCategory(storeId, categoryId, userId, dto);

        return ResponseEntity.ok(ApiResponse.success("카테고리 수정이 완료되었습니다.", categoryResponseDto));
    }

    // 카테고리 전체 수정 v1
    @PatchMapping
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> updateCategoryList(@PathVariable Long storeId, @Valid @RequestBody CategoryListRequestDto dto, @AuthUser Long userId) {

        List<CategoryResponseDto> categoryResponseDto = categoryService.updateCategoryList(storeId, userId, dto);

        return ResponseEntity.ok(ApiResponse.success("카테고리 다중 수정 v1 이 완료되었습니다.", categoryResponseDto));

    }

    // 카테고리 전체 수정 v2
    @PutMapping
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> updateCategoryListV2(@PathVariable Long storeId, @Valid @RequestBody CategoryListRequestDto dto, @AuthUser Long userId) {

        List<CategoryResponseDto> responseDtoList = categoryService.updateCategoryListV2(storeId, userId, dto);

        return ResponseEntity.ok(ApiResponse.success("카테고리 다중 수정 v2 이 완료되었습니다.", responseDtoList));
    }

    // 카테고리 단일 삭제
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Long storeId, @PathVariable Long categoryId, @AuthUser Long userId) {

        categoryService.deleteCategory(storeId, userId, categoryId);

        return ResponseEntity.ok(ApiResponse.success("카테고리 단일 삭제가 완료되었습니다."));

    }

    // 해당 가게 카테고리 전체 삭제
    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> deleteAllCategories(@PathVariable Long storeId, @AuthUser Long userId) {

        categoryService.deleteAllCategories(storeId, userId);

        return ResponseEntity.ok(ApiResponse.success("카테고리 전체 삭제가 완료되었습니다."));

    }
}
