package com.example.outsourcing.order.entity;

import com.example.outsourcing.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "order_item_option")
public class OrderItemOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer optionPrice;

    @Column(nullable = false)
    private Long orderDetailId;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private Long menuOptionId;
}
