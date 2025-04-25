package com.example.outsourcing.payment.entity;

import com.example.outsourcing.common.entity.BaseEntity;
import com.example.outsourcing.order.entity.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;
    // 취소 시간 필드
    private LocalDateTime cancelTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public void setOrder(Order order) {
        this.order = order;
    }
    public void cancel() {
        this.status = PaymentStatus.CANCELLED;
        this.cancelTime = LocalDateTime.now();
    }

}
