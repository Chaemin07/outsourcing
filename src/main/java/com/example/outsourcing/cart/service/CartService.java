package com.example.outsourcing.cart.service;

import com.example.outsourcing.cart.dto.CartRequestDto;
import com.example.outsourcing.cart.dto.UpdateCartItemRequestDto;
import com.example.outsourcing.cart.entity.Cart;
import com.example.outsourcing.cart.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;

    public void addItemtoCart(CartRequestDto cartRequestDto) {


    }

    public Long findCartIdByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않습니다!!")).getId();
    }

    @Transactional
    public void updateCart(Long userId, UpdateCartItemRequestDto requestDto) {
        Long cartId = findCartIdByUserId(userId);
    }

    // TODO 엔티티를 DTO로 변환필요

}
