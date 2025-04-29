package com.example.outsourcing.category.service;

import com.example.outsourcing.category.dto.request.CategoryListRequestDto;
import com.example.outsourcing.category.dto.request.CategoryRequestDto;
import com.example.outsourcing.category.dto.response.CategoryResponseDto;
import com.example.outsourcing.category.entity.Category;
import com.example.outsourcing.category.repository.CategoryRepository;
import com.example.outsourcing.common.enums.CategoryType;
import com.example.outsourcing.store.entity.Store;
import com.example.outsourcing.store.repository.StoreRepository;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private User user;
    private Store store;

    @BeforeEach
    public void setUp() {
        user = User.builder().id(1L).build();

        store = Store.builder().id(1L).user(user).build();
    }

    @Test
    void saveCategory_Success() {

        // given
        CategoryRequestDto requestDto = new CategoryRequestDto(CategoryType.CAFE);

        given(storeRepository.findById(store.getId())).willReturn(Optional.of(store));
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(categoryRepository.existsByStoreIdAndType(store.getId(), requestDto.getType())).willReturn(false);
        given(categoryRepository.save(any(Category.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        CategoryResponseDto responseDto = categoryService.saveCategory(store.getId(), user.getId(), requestDto);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getType()).isEqualTo(CategoryType.CAFE);
        assertThat(responseDto.getStoreId()).isEqualTo(store.getId());

        verify(categoryRepository, times(1)).save(any());
    }

    @Test
    void saveCategoryList_Success() {

        // given
        CategoryRequestDto requestDto1 = new CategoryRequestDto(CategoryType.CAFE);
        CategoryRequestDto requestDto2 = new CategoryRequestDto(CategoryType.PIZZA);
        CategoryListRequestDto requestDtoList = new CategoryListRequestDto(List.of(requestDto1, requestDto2));

        given(storeRepository.findById(store.getId())).willReturn(Optional.of(store));
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(categoryRepository.existsByStoreIdAndType(store.getId(), requestDto1.getType())).willReturn(false);
        given(categoryRepository.existsByStoreIdAndType(store.getId(), requestDto2.getType())).willReturn(false);
        given(categoryRepository.saveAll(anyList())).willAnswer(invocation -> invocation.getArgument(0));

        // when
        List<CategoryResponseDto> responseDtoList = categoryService.saveCategoryList(store.getId(), user.getId(), requestDtoList);

        // then
        assertThat(responseDtoList).hasSize(2);
        assertThat(responseDtoList.get(0).getType()).isEqualTo(CategoryType.CAFE);
        assertThat(responseDtoList.get(1).getType()).isEqualTo(CategoryType.PIZZA);

        verify(categoryRepository, times(1)).saveAll(anyList());
    }

    @Test
    void updateCategory_Success() {

        // given
        Long categoryId = 1L;
        CategoryRequestDto requestDto = new CategoryRequestDto(CategoryType.CAFE);

        Category existingCategory = new Category(CategoryType.CAFE, store);
        existingCategory.setId(categoryId);

        given(storeRepository.findById(store.getId())).willReturn(Optional.of(store));
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(existingCategory));
        given(categoryRepository.save(any(Category.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        CategoryResponseDto responseDto = categoryService.updateCategory(store.getId(), user.getId(), categoryId, requestDto);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getType()).isEqualTo(CategoryType.CAFE);
        assertThat(responseDto.getStoreId()).isEqualTo(store.getId());

        verify(categoryRepository, times(1)).save(any());
    }

    @Test
    void updateCategoryList_Success() {

        // given
        CategoryRequestDto requestDto1 = new CategoryRequestDto(CategoryType.CAFE);
        CategoryRequestDto requestDto2 = new CategoryRequestDto(CategoryType.PIZZA);
        CategoryListRequestDto requestDtoList = new CategoryListRequestDto(List.of(requestDto1, requestDto2));

        Category existingCategory = new Category(CategoryType.ASIAN, store);
        existingCategory.setId(1L);

        given(storeRepository.findById(store.getId())).willReturn(Optional.of(store));
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(categoryRepository.findByStore(store)).willReturn(List.of(existingCategory));
        given(categoryRepository.saveAll(anyList())).willAnswer(invocation -> invocation.getArgument(0));

        // when
        List<CategoryResponseDto> responseDtoList = categoryService.updateCategoryList(store.getId(), user.getId(), requestDtoList);

        // then
        assertThat(responseDtoList).hasSize(2);
        assertThat(responseDtoList.get(0).getType()).isEqualTo(CategoryType.CAFE);
        assertThat(responseDtoList.get(1).getType()).isEqualTo(CategoryType.PIZZA);

        verify(categoryRepository, times(1)).saveAll(anyList());
    }

    // @Test
    // void updateCategoryListV2_Success() {
    //
    //     // given
    //     CategoryRequestDto requestDto1 = new CategoryRequestDto(CategoryType.CAFE);
    //     CategoryRequestDto requestDto2 = new CategoryRequestDto(CategoryType.PIZZA);
    //     CategoryListRequestDto requestDtoList = new CategoryListRequestDto(List.of(requestDto1, requestDto2));
    //
    //     Category existingCategory = new Category(CategoryType.ASIAN, store);
    //     existingCategory.setId(1L);
    //
    //     given(storeRepository.findById(store.getId())).willReturn(Optional.of(store));
    //     given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
    //     given(categoryRepository.findByStore(store)).willReturn(List.of(existingCategory));
    //     given(categoryRepository.saveAll(anyList())).willAnswer(invocation -> invocation.getArgument(0));
    //
    //     // when
    //     List<CategoryResponseDto> responseDtoList = categoryService.updateCategoryListV2(store.getId(), user.getId(), requestDtoList);
    //
    //     // then
    //     assertThat(responseDtoList).hasSize(2);
    //     assertThat(responseDtoList.get(0).getType()).isEqualTo(CategoryType.CAFE);
    //     assertThat(responseDtoList.get(1).getType()).isEqualTo(CategoryType.PIZZA);
    //
    //     verify(categoryRepository, times(1)).saveAll(anyList());
    // }

    @Test
    void deleteCategory_Success() {

        // given
        Category category = new Category(CategoryType.PIZZA, store);
        category.setId(1L);
        given(storeRepository.findById(store.getId())).willReturn(Optional.of(store));
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));

        // when
        categoryService.deleteCategory(store.getId(), user.getId(), 1L);

        // then
        verify(categoryRepository, times(1)).delete(any(Category.class));
    }

    @Test
    void deleteAllCategories_Success() {

        // given
        Category category1 = new Category(CategoryType.PIZZA, store);
        Category category2 = new Category(CategoryType.CAFE, store);
        given(storeRepository.findById(store.getId())).willReturn(Optional.of(store));
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(categoryRepository.findByStore(store)).willReturn(List.of(category1, category2));

        // when
        categoryService.deleteAllCategories(store.getId(), user.getId());

        // then
        verify(categoryRepository, times(1)).deleteAll(anyList());
    }
}