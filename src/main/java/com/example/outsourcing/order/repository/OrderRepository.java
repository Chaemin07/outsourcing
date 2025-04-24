package com.example.outsourcing.order.repository;

import com.example.outsourcing.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
