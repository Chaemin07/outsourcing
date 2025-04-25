package com.example.outsourcing.category.controller;

import com.example.outsourcing.category.dto.request.CategoryListRequestDto;
import com.example.outsourcing.category.dto.request.CategoryRequestDto;
import com.example.outsourcing.category.dto.response.CategoryResponseDto;
import com.example.outsourcing.category.service.CategoryService;
import com.example.outsourcing.common.annotation.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<CategoryResponseDto> saveCategory(@PathVariable Long storeId, @Valid @RequestBody CategoryRequestDto dto, @AuthUser Long userId) {

        CategoryResponseDto categoryResponseDto = categoryService.saveCategory(storeId, userId, dto);

        return new ResponseEntity<>(categoryResponseDto, HttpStatus.OK);
    }

    // 카테고리 다중 추가
    @PostMapping("/lists")
    public ResponseEntity<List<CategoryResponseDto>> saveCategoryList(@PathVariable Long storeId, @Valid @RequestBody CategoryListRequestDto dto, @AuthUser Long userId) {

        List<CategoryResponseDto> categoryResponseDto = categoryService.saveCategoryList(storeId, userId, dto);

        return new ResponseEntity<>(categoryResponseDto, HttpStatus.OK);
    }

    // 카테고리 단일 수정
    @PatchMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Long storeId, @PathVariable Long categoryId, @Valid @RequestBody CategoryRequestDto dto, @AuthUser Long userId) {

        CategoryResponseDto categoryResponseDto = categoryService.updateCategory(storeId, categoryId, userId, dto);

        return new ResponseEntity<>(categoryResponseDto, HttpStatus.OK);
    }

    // 카테고리 전체 수정 v1
    @PatchMapping
    public ResponseEntity<List<CategoryResponseDto>> updateCategoryList(@PathVariable Long storeId, @Valid @RequestBody CategoryListRequestDto dto, @AuthUser Long userId) {

        List<CategoryResponseDto> categoryResponseDto = categoryService.updateCategoryList(storeId, userId, dto);

        return new ResponseEntity<>(categoryResponseDto, HttpStatus.OK);
    }

    // 카테고리 전체 수정 v2
    @PutMapping
    public ResponseEntity<List<CategoryResponseDto>> updateCategoryListV2(@PathVariable Long storeId, @Valid @RequestBody CategoryListRequestDto dto, @AuthUser Long userId) {

        List<CategoryResponseDto> responseDtoList = categoryService.updateCategoryListV2(storeId, userId, dto);

        return new ResponseEntity<>(responseDtoList, HttpStatus.OK);
    }

    // 카테고리 단일 삭제
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long storeId, @PathVariable Long categoryId, @AuthUser Long userId) {

        categoryService.deleteCategory(storeId, userId, categoryId);

        return new ResponseEntity<>("해당 카테고리 삭제가 완료되었습니다.", HttpStatus.OK);
    }

    // 해당 가게 카테고리 전체 삭제
    @DeleteMapping
    public ResponseEntity<String> deleteAllCategories(@PathVariable Long storeId, @AuthUser Long userId) {

        categoryService.deleteAllCategories(storeId, userId);

        return new ResponseEntity<>("해당 가게의 카테고리가 삭제되었습니다.", HttpStatus.OK);
    }
}
