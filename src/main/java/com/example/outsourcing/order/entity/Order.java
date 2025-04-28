package com.example.outsourcing.order.entity;

import com.example.outsourcing.common.entity.BaseEntity;
import com.example.outsourcing.common.enums.DeliveryStatus;
import com.example.outsourcing.common.enums.OrderStatus;
import com.example.outsourcing.payment.entity.Payment;
import com.example.outsourcing.store.entity.Store;
import com.example.outsourcing.user.entity.Role;
import com.example.outsourcing.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="store_id",nullable = false)
    private Store store;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetailList = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Payment payment;

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public void setCanceledBy(Role canceledBy) {
        this.canceledBy = canceledBy;
    }

    public void setCancelReason(String cancelReason) {
        this.canceledReason = cancelReason;
    }
    public void setPayment(Payment payment) {
        this.payment = payment;
        payment.setOrder(this);
    }
}
