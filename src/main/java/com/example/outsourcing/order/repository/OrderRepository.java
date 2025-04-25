package com.example.outsourcing.order.repository;

import com.example.outsourcing.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    //가게 id로 주문 조회하기(사장님)
    List<Order> findByStoreId(Long storeId);
}
