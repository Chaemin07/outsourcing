package com.example.outsourcing.category.service;

import com.example.outsourcing.category.dto.request.CategoryListRequestDto;
import com.example.outsourcing.category.dto.request.CategoryRequestDto;
import com.example.outsourcing.category.dto.response.CategoryResponseDto;
import com.example.outsourcing.category.entity.Category;
import com.example.outsourcing.category.repository.CategoryRepository;
import com.example.outsourcing.common.enums.CategoryType;
import com.example.outsourcing.review.entity.Review;
import com.example.outsourcing.store.entity.Store;
import com.example.outsourcing.store.repository.StoreRepository;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;

    // 카테고리 추가
    public CategoryResponseDto saveCategory(Long storeId, Long userId, CategoryRequestDto dto) {

        // storeId에 해당하는 가게가 없을 때 예외처리
        Store store = getStoreByStoreId(storeId);

        // userId에 해당하는 유저가 없을 때 예외처리
        User user = getUserByUserId(userId);
        
        // 해당 가게의 주인이 아니면 예외처리
        validateMyStore(store, userId);
        
        // 중복된 카테고리가 있으면 예외처리
        checkDuplicatedCategory(storeId, dto);

        Category category = categoryRepository.save(new Category(dto.getType(), store));

        return new CategoryResponseDto(category.getId(), category.getType(), store.getId());
    }

    // 카테고리 다중 추가
    @Transactional
    public List<CategoryResponseDto> saveCategoryList(Long storeId, Long userId, CategoryListRequestDto dto) {
        List<CategoryRequestDto> requestDtoList = dto.getTypeList();

        // storeId에 해당하는 가게가 없을 때 예외처리
        Store store = getStoreByStoreId(storeId);

        // userId에 해당하는 유저가 없을 때 예외처리
        User user = getUserByUserId(userId);

        // 해당 가게의 주인이 아니면 예외처리
        validateMyStore(store, userId);

        for (CategoryRequestDto requestDto : requestDtoList) {
            // 중복된 카테고리가 있으면 예외처리
            checkDuplicatedCategory(storeId, requestDto);
        }

        // 들어온 카테고리 리스트를 Entity로 변환 저장
        List<Category> categories = dto.getTypeList().stream()
                .map(requestDto -> new Category(requestDto.getType(), store))
                .collect(Collectors.toList());

        // 카테고리들을 dto로 변환 후 반환
        return categoryRepository.saveAll(categories).stream()
                .map(CategoryResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryResponseDto updateCategory(Long storeId, Long userId, Long categoryId, CategoryRequestDto dto) {

        // storeId에 해당하는 가게가 없을 때 예외처리
        Store store = getStoreByStoreId(storeId);

        // userId에 해당하는 유저가 없을 때 예외처리
        User user = getUserByUserId(userId);

        // categoryId에 해당하는 카테고리가 없을 때 예외처리
        Category category = getCategoryByCategoryId(categoryId);

        // 중복된 카테고리가 있으면 예외처리
        checkDuplicatedCategory(storeId, dto);

        // 해당 가게의 주인이 아니면 예외처리
        validateMyStore(store, userId);

        CategoryType newType = CategoryType.from(String.valueOf(dto.getType()));

        category.setType(newType);

        Category updated = categoryRepository.save(category);

        return new CategoryResponseDto(updated);
    }

    @Transactional
    public List<CategoryResponseDto> updateCategoryList(Long storeId, Long userId, CategoryListRequestDto dto) {

        // storeId에 해당하는 가게가 없을 때 예외처리
        Store store = getStoreByStoreId(storeId);

        // userId에 해당하는 유저가 없을 때 예외처리
        User user = getUserByUserId(userId);

        // 해당 가게의 주인이 아니면 예외처리
        validateMyStore(store, userId);

        // 수정할 카테고리가 없으면(카테고리가 비어있으면) 예외처리
        validateCategoryNotEmpty(dto);

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
    public List<CategoryResponseDto> updateCategoryListV2(Long storeId, Long userId, CategoryListRequestDto dto) {

        // storeId에 해당하는 가게가 없을 때 예외처리
        Store store = getStoreByStoreId(storeId);

        // userId에 해당하는 유저가 없을 때 예외처리
        User user = getUserByUserId(userId);

        // 해당 가게의 주인이 아니면 예외처리
        validateMyStore(store, userId);

        // 수정할 카테고리가 없으면(카테고리가 비어있으면) 예외처리
        validateCategoryNotEmpty(dto);

        List<Category> categoryList = categoryRepository.findByStore(store);

        List<CategoryRequestDto> newTypeList = dto.getTypeList();

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

    public void deleteCategory(Long storeId, Long userId, Long categoryId) {

        // storeId에 해당하는 가게가 없을 때 예외처리
        Store store = getStoreByStoreId(storeId);

        // 해당 가게의 주인이 아니면 예외처리
        validateMyStore(store, userId);

        // userId에 해당하는 유저가 없을 때 예외처리
        User user = getUserByUserId(userId);

        // categoryId에 해당하는 카테고리가 없을 때 예외처리
        Category category = getCategoryByCategoryId(categoryId);

        categoryRepository.delete(category);
    }

    public void deleteAllCategories(Long storeId, Long userId) {

        // storeId에 해당하는 가게가 없을 때 예외처리
        Store store = getStoreByStoreId(storeId);

        // 해당 가게의 주인이 아니면 예외처리
        validateMyStore(store, userId);

        // userId에 해당하는 유저가 없을 때 예외처리
        User user = getUserByUserId(userId);

        List<Category> categories = categoryRepository.findByStore(store);
        // 삭제할 카테고리가 없으면(카테고리가 비어있으면) 예외처리
        validateCategoryNotEmpty(categories);

        categoryRepository.deleteAll(categories);
    }

    public Store getStoreByStoreId(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException("가게가 존재하지 않습니다."));
    }

    public Category getCategoryByCategoryId(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("카테고리가 존재하지 않습니다."));
    }

    public User getUserByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));
    }

    public void checkDuplicatedCategory(Long storeId, CategoryRequestDto dto) {
        if (categoryRepository.existsByStoreIdAndType(storeId, dto.getType())) {
            throw new RuntimeException("중복된 카테고리 입니다.");
        }
    }

    public void validateMyStore(Store store, Long userId) {
        if (!store.getUser().getId().equals(userId)) {
            throw new RuntimeException("본인 가게가 아닙니다.");
        }
    }

    public void validateCategoryNotEmpty(List<Category> categoryList) {
        if (categoryList.isEmpty()) {
            throw new RuntimeException("카테고리가 비어있습니다.");
        }
    }

    public void validateCategoryNotEmpty(CategoryListRequestDto dto) {
        if (dto.getTypeList() == null || dto.getTypeList().isEmpty()) {
            throw new RuntimeException("카테고리가 비어있습니다.");
        }
    }
}
