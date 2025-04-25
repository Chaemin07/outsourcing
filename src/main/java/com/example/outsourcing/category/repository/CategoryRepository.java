package com.example.outsourcing.category.repository;

import com.example.outsourcing.category.entity.Category;
import com.example.outsourcing.common.enums.CategoryType;
import com.example.outsourcing.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByStore(Store store);

    boolean existsByStoreIdAndType(Long storeId, CategoryType type);
}
