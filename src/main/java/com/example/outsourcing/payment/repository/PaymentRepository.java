package com.example.outsourcing.payment.repository;

import com.example.outsourcing.order.entity.Order;
import com.example.outsourcing.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
