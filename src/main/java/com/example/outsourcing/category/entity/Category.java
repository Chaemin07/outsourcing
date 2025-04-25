package com.example.outsourcing.category.entity;

import com.example.outsourcing.common.enums.CategoryType;
import com.example.outsourcing.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CategoryType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public Category(CategoryType type, Store store) {
        this.type = type;
        this.store = store;
    }

    public void updateType(CategoryType type) {
        this.type = type;
    }
}
