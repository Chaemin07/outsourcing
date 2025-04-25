package com.example.outsourcing.order.entity;

import com.example.outsourcing.common.entity.BaseEntity;
import com.example.outsourcing.user.entity.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 요청 메세지는 null 가능
    private String requestMessage;

    @Column(nullable = false)
    private String deliveryAddress;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Enumerated(EnumType.STRING)
    private Role canceledBy; // 주문 취소한 사용자 역할

    private String canceledReason;

    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long storeId;


}
