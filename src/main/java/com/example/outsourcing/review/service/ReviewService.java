package com.example.outsourcing.review.service;

import com.example.outsourcing.order.entity.Order;
import com.example.outsourcing.review.dto.request.ReviewRequestDto;
import com.example.outsourcing.review.dto.response.ReviewResponseDto;
import com.example.outsourcing.review.entity.Review;
import com.example.outsourcing.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    // 주문 레포

    public ReviewResponseDto saveReview(Long storeId, Long userId, Long orderId, ReviewRequestDto dto) {

        // 주문이 배달 완료가 아닐 경우 리뷰 작성 불가

        // Order order = 주문 레포에서 주문 찾아오기
        Order order = new Order();
        order.setId(orderId);

        Review savedReview = new Review(dto.getContent(), dto.getScore(), order);

        Review saved = reviewRepository.save(savedReview);

        return new ReviewResponseDto(savedReview);
    }
}
