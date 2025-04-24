package com.example.outsourcing.order.entity;

import com.example.outsourcing.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "order")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 요청 메세지는 null 가능
    private String requestMessage;

    @Column(nullable = false)
    private String deliveryAddress;

    @Column(nullable = false)
    private String orderStatus;

    @Column(nullable = false)
    private String deliveryStatus;

    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long storeId;


}
