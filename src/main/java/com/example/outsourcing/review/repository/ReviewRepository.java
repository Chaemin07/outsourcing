package com.example.outsourcing.review.repository;

import com.example.outsourcing.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Long 값의 ReviewId 만을 찾기 위해서는 쿼리 작성 필요
    Optional<Review> findByOrderId(Long orderId);

    List<Review> findByStoreId(Long storeId);
}
