package com.example.outsourcing.review.service;

import com.example.outsourcing.order.entity.Order;
import com.example.outsourcing.order.repository.OrderRepository;
import com.example.outsourcing.review.dto.request.ReviewRequestDto;
import com.example.outsourcing.review.dto.response.ReviewResponseDto;
import com.example.outsourcing.review.entity.Review;
import com.example.outsourcing.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    public ReviewResponseDto saveReview(Long userId, Long orderId, ReviewRequestDto dto) {

        // orderId로 userId, storeId 조회

        // 본인의 주문이 맞는 지 확인(userId == orderId.getUserId())
        // 주문이 배달 완료가 아닐 경우 리뷰 작성 불가

        // Order order = 주문 레포에서 주문 찾아오기
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문 없음"));

        Review savedReview = new Review(dto.getContent(), dto.getScore(), order);

        Review saved = reviewRepository.save(savedReview);

        return new ReviewResponseDto(savedReview);
    }

    public void deleteReview(Long reviewId, String password, String userPassword) {

        // Bcrypt => 비밀번호 matches로 검증
        if (!password.equals(userPassword)) {

            // 예외처리
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        reviewRepository.deleteById(reviewId);
    }
}
