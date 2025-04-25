package com.example.outsourcing.category.controller;

import com.example.outsourcing.category.dto.request.CategoryListRequestDto;
import com.example.outsourcing.category.dto.request.CategoryRequestDto;
import com.example.outsourcing.category.dto.response.CategoryResponseDto;
import com.example.outsourcing.category.service.CategoryService;
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

    @PostMapping
    public ResponseEntity<CategoryResponseDto> saveCategory(@PathVariable Long storeId, @Valid @RequestBody CategoryRequestDto dto) {

        CategoryResponseDto categoryResponseDto = categoryService.saveCategory(storeId, dto);

        return new ResponseEntity<>(categoryResponseDto, HttpStatus.OK);
    }

    @PostMapping("/lists")
    public ResponseEntity<List<CategoryResponseDto>> saveCategoryList(@PathVariable Long storeId, @Valid @RequestBody CategoryListRequestDto dto) {

        List<CategoryResponseDto> categoryResponseDto = categoryService.saveCategoryList(storeId, dto);

        return new ResponseEntity<>(categoryResponseDto, HttpStatus.OK);
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Long storeId, @PathVariable Long categoryId, @Valid @RequestBody CategoryRequestDto dto) {

        CategoryResponseDto categoryResponseDto = categoryService.updateCategory(storeId, categoryId, dto);

        return new ResponseEntity<>(categoryResponseDto, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<List<CategoryResponseDto>> updateCategoryList(@PathVariable Long storeId, @Valid @RequestBody CategoryListRequestDto dto) {

        List<CategoryResponseDto> categoryResponseDto = categoryService.updateCategoryList(storeId, dto);

        return new ResponseEntity<>(categoryResponseDto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<List<CategoryResponseDto>> updateCategoryListV2(@PathVariable Long storeId, @Valid @RequestBody CategoryListRequestDto dto) {

        List<CategoryResponseDto> responseDtoList = categoryService.updateCategoryListV2(storeId, dto);

        return new ResponseEntity<>(responseDtoList, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long storeId, @PathVariable Long categoryId) {

        categoryService.deleteCategory(storeId, categoryId);

        return new ResponseEntity<>("해당 카테고리 삭제가 완료되었습니다.", HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllCategories(@PathVariable Long storeId) {

        categoryService.deleteAllCategories(storeId);

        return new ResponseEntity<>("해당 가게의 카테고리가 삭제되었습니다.", HttpStatus.OK);
    }
}
