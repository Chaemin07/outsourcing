package com.example.outsourcing.review.service;

import com.example.outsourcing.order.entity.Order;
import com.example.outsourcing.order.repository.OrderRepository;
import com.example.outsourcing.review.dto.request.ReviewRequestDto;
import com.example.outsourcing.review.dto.response.ReviewListResponseDto;
import com.example.outsourcing.review.dto.response.ReviewResponseDto;
import com.example.outsourcing.review.entity.Review;
import com.example.outsourcing.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        Review savedReview = new Review(dto.getContent(), dto.getScore(), order.getStoreId(), order);

        Review saved = reviewRepository.save(savedReview);

        return new ReviewResponseDto(savedReview);
    }

    @Transactional
    public ReviewResponseDto updateReview(Long userId, Long orderId, ReviewRequestDto dto) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문 없음"));

        // 본인의 리뷰가 맞는 지 확인
        // => 본인의 주문이 맞는 지 확인 (주문 <-> 리뷰 / 1ㄷ1 매핑) 즉, 주문당 1개의 리뷰
        // save의 확인이랑 묶어서 메서드화?

        Review savedReview = reviewRepository.findByOrderId(orderId);

        savedReview.updateReview(dto.getContent(), dto.getScore());

        // 수정 기간은 주문후 3일 - 주문의 created_at

        return new ReviewResponseDto(savedReview);
    }

    public void deleteReview(Long userId, Long orderId, String password) {

        String userPassword = "1234";

        // Bcrypt => 비밀번호 matches로 검증
        if (!password.equals(userPassword)) {

            // 예외처리
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // orderId로 reviewId를 찾아옴
        // 해당 orderId에 대한 리뷰가 없을 때 예외처리
        // 해당 orderId에 해당하는 주문이 없을 때 예외처리
        Long reviewId = reviewRepository.findByOrderId(orderId).getId();

        reviewRepository.deleteById(reviewId);
    }

    public ReviewListResponseDto getReviewsByStoreId(Long storeId) {

        List<Review> reviews = reviewRepository.findByStoreId(storeId);

        List<ReviewResponseDto> reviewList = reviews.stream().map(this::convertToDto).toList();

        Long count = (long) reviewList.size();
        double average = reviewList.stream().mapToInt(ReviewResponseDto::getScore).average().orElse(0.0);

        return new ReviewListResponseDto(count, average, reviewList);
    }

    private ReviewResponseDto convertToDto(Review review) {
        return new ReviewResponseDto(
                review.getId(),
                review.getContent(),
                review.getScore(),
                // 음식 이름 추가
                // 이미지 주소 추가
                review.getOrder().getUserId(),
                review.getOrder().getId(),
                review.getStoreId()
        );
    }
}
