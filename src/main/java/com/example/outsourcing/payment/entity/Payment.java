package com.example.outsourcing.payment.entity;

import com.example.outsourcing.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "payment")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private String status;
}
