package com.example.outsourcing.order.entity;

import com.example.outsourcing.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "order_detail")
public class OrderDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long menuId;
}
