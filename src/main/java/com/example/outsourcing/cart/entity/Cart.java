package com.example.outsourcing.cart.entity;

import com.example.outsourcing.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "cart")
public class Cart extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private Long menuId;


}
