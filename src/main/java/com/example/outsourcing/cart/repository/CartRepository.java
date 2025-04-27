package com.example.outsourcing.cart.repository;

import com.example.outsourcing.cart.entity.Cart;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {

    Optional<Cart> findByUserIdAndMenuId(Long userId, Long menuId);

    List<Cart> findByUserId(Long userId);

    void deleteByUserId(Long userId);

    // 사용자 id를 기준으로 마지막 장바구니의 수정시간 가져오기
    @Query("SELECT MAX(c.updatedAt) FROM Cart c WHERE c.userId = :userId")
    Optional<LocalDateTime> findMaxUpdatedAtByUserId(@Param("userId") Long userId);
}
