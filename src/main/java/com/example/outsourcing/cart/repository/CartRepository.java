package com.example.outsourcing.cart.repository;

import com.example.outsourcing.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {

    Optional<Cart> findByUserIdAndMenuId(Long userId, Long menuId);

    List<Cart> findByUserId(Long userId);

    void deleteByUserId(Long userId);

}
