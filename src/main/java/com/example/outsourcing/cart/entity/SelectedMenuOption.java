package com.example.outsourcing.cart.entity;

import com.example.outsourcing.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "selected_menu_option")
public class SelectedMenuOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long cartId;

    @Column(nullable = false)
    private Long menuOptionId;

    @Column(nullable = false)
    private Long menuId;


}
