package com.example.outsourcing.review.service;

import com.example.outsourcing.common.enums.SortType;
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

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    public ReviewResponseDto saveReview(Long userId, Long orderId, ReviewRequestDto dto) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문 없음"));

        // TODO: 본인의 주문이 맞는 지 확인(userId == orderId.getUserId())
        // userId => 로그인 되이었는 유저의 userId
        // orderId.getUserId => 주문을 한 유저의 userID

        if (!order.getDeliveryStatus().equals("DELIVERED")) {
            // 배송 완료가 아닐시 예외 처리
            throw new RuntimeException("배송이 완료되지 않았습니다.");
        }

        LocalDateTime deliveredAt = order.getUpdatedAt();
        isReviewPeriodExpired(deliveredAt);

        Review savedReview = new Review(dto.getContent(), dto.getScore(), order.getStoreId(), order);

        Review saved = reviewRepository.save(savedReview);

        return new ReviewResponseDto(savedReview);
    }

    @Transactional
    public ReviewResponseDto updateReview(Long userId, Long orderId, ReviewRequestDto dto) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문 없음"));

        // TODO: 본인의 주문이 맞는 지 확인(userId == orderId.getUserId())
        // userId => 로그인 되이었는 유저의 userId
        // orderId.getUserId => 주문을 한 유저의 userID
        // save의 확인이랑 묶어서 메서드화?

        LocalDateTime deliveredAt = order.getUpdatedAt();
        isReviewPeriodExpired(deliveredAt);

        Review savedReview = reviewRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("해당 주문에 대한 리뷰가 없습니다."));

        savedReview.updateReview(dto.getContent(), dto.getScore());

        return new ReviewResponseDto(savedReview);
    }

    public void deleteReview(Long userId, Long orderId, String password) {

        // TODO: 본인의 주문이 맞는 지 확인(userId == orderId.getUserId())
        // userId => 로그인 되이었는 유저의 userId
        // orderId.getUserId => 주문을 한 유저의 userID

        String userPassword = "1234";

        // TODO: Bcrypt => 비밀번호 matches로 검증
        if (!password.equals(userPassword)) {

            // 예외처리
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // orderId에 해당하는 주문이 없을 때 예외처리
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문이 존재하지 않습니다."));
        // orderId에 대한 리뷰가 없을 때 예외처리
        Review savedReview = reviewRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("해당 주문에 대한 리뷰가 없습니다."));

        Long reviewId = savedReview.getId();

        reviewRepository.deleteById(reviewId);
    }

    public ReviewListResponseDto getReviewsByStoreId(Long storeId, SortType sortType) {

        List<Review> reviews = reviewRepository.findByStoreId(storeId);

        Stream<Review> stream = reviews.stream();

        // TODO: 이미지가 있는 리뷰만 보기
        // if (sortType == SortType.WITH_IMAGES) {
        //     stream = stream.filter(review -> review.getImage)
        // }

        if (SORT_COMPARATORS.containsKey(sortType)) {
            stream = stream.sorted(SORT_COMPARATORS.get(sortType));
        }

        List<ReviewResponseDto> reviewList = stream.map(this::convertToDto).toList();

        Long count = (long) reviewList.size();
        double average = reviewList.stream().mapToInt(ReviewResponseDto::getScore).average().orElse(0.0);

        return new ReviewListResponseDto(count, average, reviewList);
    }

    // private ReviewResponseDto convertToDto(Review review) {
    //     return new ReviewResponseDto(
    //             review.getId(),
    //             review.getContent(),
    //             review.getScore(),
    //             // 음식 이름 추가
    //             // 이미지 주소 추가
    //             review.getOrder().getUserId(),
    //             review.getOrder().getId(),
    //             review.getStoreId(),
    //             review.getReviewComment().getContent()
    //     );
    // }

    private ReviewResponseDto convertToDto(Review review) {
        return new ReviewResponseDto(review);
    }

    private final Map<SortType, Comparator<Review>> SORT_COMPARATORS = Map.of(
            SortType.SCORE_DESC, Comparator.comparingInt(Review::getScore).reversed(),
            SortType.SCORE_ASC, Comparator.comparingInt(Review::getScore),
            SortType.LATEST, Comparator.comparing(Review::getCreatedAt).reversed()
    );

    public void isReviewPeriodExpired(LocalDateTime deliveredAt) {
        // 테스트를 위해 1분 뒤로 변경.
        // 3일뒤 => plusDays(3)
        if (deliveredAt.plusMinutes(1).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("배송 완료 후 3일이 지났습니다.");
        }
    }
}
