package com.example.outsourcing.category.service;

import com.example.outsourcing.category.dto.request.CategoryListRequestDto;
import com.example.outsourcing.category.dto.request.CategoryRequestDto;
import com.example.outsourcing.category.dto.response.CategoryResponseDto;
import com.example.outsourcing.category.entity.Category;
import com.example.outsourcing.category.repository.CategoryRepository;
import com.example.outsourcing.common.enums.CategoryType;
import com.example.outsourcing.store.entity.Store;
import com.example.outsourcing.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;

    public CategoryResponseDto saveCategory(Long storeId, CategoryRequestDto dto) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("없는 가게입니다."));
        
        // TODO: 본인 가게인지 확인

        if (categoryRepository.existsByStoreIdAndType(storeId, dto.getType())) {
            throw new RuntimeException("해당 가게는 이미 이 카테고리를 가지고 있음");
        }

        Category category = categoryRepository.save(new Category(dto.getType(), store));

        return new CategoryResponseDto(category.getId(), category.getType(), store.getId());
    }

    @Transactional
    public List<CategoryResponseDto> saveCategoryList(Long storeId, CategoryListRequestDto dto) {
        List<CategoryRequestDto> requestDtoList = dto.getTypeList();

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("없는 가게입니다."));

        // TODO: 본인 가게인지 확인

        for (CategoryRequestDto requestDto : requestDtoList) {
            CategoryType type = CategoryType.from(String.valueOf(requestDto.getType()));
            // TODO: 메서드화
            if (categoryRepository.existsByStoreIdAndType(storeId, type)) {
                throw new RuntimeException("해당 가게는 이미 이 카테고리를 가지고 있음" + type);
            }
        }

        List<Category> categories = dto.getTypeList().stream()
                .map(requestDto -> new Category(requestDto.getType(), store))
                .collect(Collectors.toList());

        return categoryRepository.saveAll(categories).stream()
                .map(CategoryResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryResponseDto updateCategory(Long storeId, Long categoryId, CategoryRequestDto dto) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("없는 가게입니다."));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("없는 카테고리 입니다."));

        CategoryType newType = CategoryType.from(String.valueOf(dto.getType()));

        if (categoryRepository.existsByStoreIdAndType(storeId, newType)) {
            throw new RuntimeException("해당 가게는 이미 이 카테고리를 가지고 있음" + newType);
        }

        category.setType(newType);

        Category updated = categoryRepository.save(category);

        return new CategoryResponseDto(updated);
    }

    @Transactional
    public List<CategoryResponseDto> updateCategoryList(Long storeId, CategoryListRequestDto dto) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("없는 가게입니다."));

        if (dto.getTypeList().isEmpty()) {
            throw new RuntimeException("수정할 카테고리가 없습니다.");
        }

        List<Category> categoryList = categoryRepository.findByStore(store);
        categoryRepository.deleteAll(categoryList);

        List<Category> categories = dto.getTypeList().stream()
                .map(requestDto -> new Category(requestDto.getType(), store))
                .collect(Collectors.toList());

        return categoryRepository.saveAll(categories).stream()
                .map(CategoryResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CategoryResponseDto> updateCategoryListV2(Long storeId, CategoryListRequestDto dto) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("없는 가게입니다."));

        List<Category> categoryList = categoryRepository.findByStore(store);

        List<CategoryRequestDto> newTypeList = dto.getTypeList();

        if (newTypeList.isEmpty()) {
            throw new RuntimeException("수정 할 카테고리가 없습니다.");
        }

        Set<String> typeCheck = new HashSet<>();
        for (CategoryRequestDto temp : newTypeList) {
            String upper = temp.getType().toString().toUpperCase();
            if (!typeCheck.add(upper)) {
                throw new RuntimeException("중복된 카테고리 존재");
            }
        }

        int size = categoryList.size();
        int newSize = newTypeList.size();

        for (int i = 0; i < Math.min(size, newSize); i++) {
            CategoryType type = newTypeList.get(i).getType();
            categoryList.get(i).setType(type);
        }

        if (newSize > size) {
            for (int i = size; i < newSize; i++) {
                CategoryType type = newTypeList.get(i).getType();
                Category newCategory = new Category(type, store);
                categoryList.add(categoryRepository.save(newCategory));
            }
        }

        if (size > newSize) {
            List<Category> delete = categoryList.subList(newSize, size);
            categoryRepository.deleteAll(delete);
            categoryList = categoryList.subList(0, newSize);
        }

        return categoryList.stream().map(CategoryResponseDto::new)
                .collect(Collectors.toList());
    }

    public void deleteCategory(Long storeId, Long categoryId) {

        // TODO: 본인 가게인지 확인

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("없는 가게입니다."));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("없는 카테고리 입니다."));

        categoryRepository.delete(category);
    }

    public void deleteAllCategories(Long storeId) {

        // TODO: 본인 가게인지 확인

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("없는 가게입니다."));

        List<Category> categories = categoryRepository.findByStore(store);
        if (categories.isEmpty()) {
            throw new RuntimeException("삭제할 카테고리가 없습니다.");
        }

        categoryRepository.deleteAll(categories);
    }
}
