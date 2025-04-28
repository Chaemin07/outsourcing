package com.example.outsourcing.review.service;

import com.example.outsourcing.common.config.PasswordEncoder;
import com.example.outsourcing.common.enums.SortType;
import com.example.outsourcing.common.exception.BaseException;
import com.example.outsourcing.common.exception.ErrorCode;
import com.example.outsourcing.image.service.ImageService;
import com.example.outsourcing.order.entity.DeliveryStatus;
import com.example.outsourcing.order.entity.Order;
import com.example.outsourcing.order.repository.OrderRepository;
import com.example.outsourcing.review.dto.request.ReviewRequestDto;
import com.example.outsourcing.review.dto.response.ReviewListResponseDto;
import com.example.outsourcing.review.dto.response.ReviewResponseDto;
import com.example.outsourcing.review.entity.Review;
import com.example.outsourcing.review.repository.ReviewRepository;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    // 리뷰 저장
    public ReviewResponseDto saveReview(Long userId, Long orderId, ReviewRequestDto dto) {

        // orderId에 해당하는 주문이 없을 때 예외처리
        Order order = getOrderByOrderId(orderId);

        // userId에 해당하는 유저가 없을 때 예외처리
        User user = getUserByUserId(userId);

        // 본인의 주문이 아니면 예외처리
        checkMyOrder(order, userId);

        if (!order.getDeliveryStatus().equals(DeliveryStatus.DELIVERED)) {
            // 배송 완료가 아닐시 예외 처리
            throw new BaseException(ErrorCode.NOT_DELIVERED);
        }

        // 배송이 완료된지 일정 시간이 지나면 예외처리
        LocalDateTime deliveredAt = order.getUpdatedAt();
        isReviewPeriodExpired(deliveredAt);

        Review savedReview = new Review(dto.getContent(), dto.getScore(), order.getStore().getId(), order);

        Review saved = reviewRepository.save(savedReview);

        return new ReviewResponseDto(savedReview);
    }

    @Transactional
    public void uploadReviewImage(Long userId, Long orderId, MultipartFile file) {

        Order order = getOrderByOrderId(orderId);

        User user = getUserByUserId(userId);

        Review review = getReviewByOrderId(orderId);

        checkMyOrder(order, userId);

        review.setImage(imageService.uploadImage(file));
    }

    @Transactional
    public ReviewResponseDto updateReview(Long userId, Long orderId, ReviewRequestDto dto) {

        // orderId에 해당하는 주문이 없을 때 예외처리
        Order order = getOrderByOrderId(orderId);

        // userId에 해당하는 유저가 없을 때 예외처리
        User user = getUserByUserId(userId);

        // orderId에 해당하는 리뷰가 없을 때 예외처리
        Review review = getReviewByOrderId(orderId);

        // 본인의 주문이 아니면 예외처리
        checkMyOrder(order, userId);

        // 배송이 완료된지 일정 시간이 지나면 예외처리
        LocalDateTime deliveredAt = order.getUpdatedAt();
        isReviewPeriodExpired(deliveredAt);

        review.updateReview(dto.getContent(), dto.getScore());

        return new ReviewResponseDto(review);
    }

    public void deleteReview(Long userId, Long orderId, String password) {

        // orderId에 해당하는 주문이 없을 때 예외처리
        Order order = getOrderByOrderId(orderId);

        // userId에 해당하는 유저가 없을 때 예외처리
        User user = getUserByUserId(userId);

        // orderId에 해당하는 리뷰가 없을 때 예외처리
        Review review = getReviewByOrderId(orderId);

        // 본인의 주문이 아니면 예외처리
        checkMyOrder(order, userId);

        String userPassword = user.getPassword();

        if (!passwordEncoder.matches(password, userPassword)) {
            throw new BaseException(ErrorCode.INVALID_PASSWORD);
        }

        Long reviewId = review.getId();

        reviewRepository.deleteById(reviewId);
    }

    public ReviewListResponseDto getReviewsByStoreId(Long storeId, SortType sortType) {

        List<Review> reviews = reviewRepository.findByStoreId(storeId);

        Stream<Review> stream = reviews.stream();

        if (sortType == SortType.WITH_IMAGES) {
            stream = stream.filter(review -> review.getImage() != null && !review.getImage().getPath().isEmpty());
        }

        if (SORT_COMPARATORS.containsKey(sortType)) {
            stream = stream.sorted(SORT_COMPARATORS.get(sortType));
        }

        List<ReviewResponseDto> reviewList = stream.map(this::convertToDto).toList();

        Long count = (long) reviewList.size();
        double average = reviewList.stream().mapToInt(ReviewResponseDto::getScore).average().orElse(0.0);

        return new ReviewListResponseDto(count, average, reviewList);
    }

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

    public Order getOrderByOrderId(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_ORDER_ID));
    }

    public User getUserByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER_ID));
    }

    public Review getReviewByOrderId(Long orderId) {
        return reviewRepository.findByOrderId(orderId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_REVIEW));
    }

    public void checkMyOrder(Order order, Long userId) {
        if (!order.getUser().getId().equals(userId)) {
            throw new BaseException(ErrorCode.NOT_CUSTOMER);
        }
    }
}
