package com.example.outsourcing.reviewcomment.service;

import com.example.outsourcing.common.config.PasswordEncoder;
import com.example.outsourcing.common.exception.BaseException;
import com.example.outsourcing.common.exception.ErrorCode;
import com.example.outsourcing.review.entity.Review;
import com.example.outsourcing.review.repository.ReviewRepository;
import com.example.outsourcing.reviewcomment.dto.request.ReviewCommentRequestDto;
import com.example.outsourcing.reviewcomment.dto.response.ReviewCommentResponseDto;
import com.example.outsourcing.reviewcomment.entity.ReviewComment;
import com.example.outsourcing.reviewcomment.repository.ReviewCommentRepository;
import com.example.outsourcing.store.entity.Store;
import com.example.outsourcing.store.repository.StoreRepository;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewCommentService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final PasswordEncoder passwordEncoder;

    public ReviewCommentResponseDto saveReviewComment(Long userId, Long reviewId, ReviewCommentRequestDto dto) {

        User user = getUserByUserId(userId);
        Review review = getReviewByReviewId(reviewId);
        Store store = getStoreByStoreId(review.getStoreId());

        // 해당 가게의 사장님이 아닌 유저는 리뷰 댓글 작성불가
        validateMyStore(store, userId);

        ReviewComment savedReviewComment = new ReviewComment(dto.getContent(), review);

        ReviewComment saved = reviewCommentRepository.save(savedReviewComment);

        return new ReviewCommentResponseDto(reviewId, savedReviewComment.getContent());
    }

    @Transactional
    public ReviewCommentResponseDto updateReviewComment(Long userId, Long reviewId, ReviewCommentRequestDto dto) {

        Review review = getReviewByReviewId(reviewId);
        Store store = getStoreByStoreId(review.getStoreId());
        ReviewComment reviewComment = getReviewCommentByReviewId(reviewId);

        // 해당 가게의 사장님이 아닌 유저는 리뷰 댓글 수정불가
        validateMyStore(store, userId);

        reviewComment.updateReviewComment(dto.getContent());

        return new ReviewCommentResponseDto(reviewComment.getId(), reviewComment.getContent());
    }

    public void deleteReviewComment(Long userId, Long reviewId, String password) {

        User user = getUserByUserId(userId);
        Review review = getReviewByReviewId(reviewId);
        Store store = getStoreByStoreId(review.getStoreId());
        ReviewComment reviewComment = getReviewCommentByReviewId(reviewId);

        // 해당 가게의 사장님이 아닌 유저는 리뷰 댓글 삭제
        validateMyStore(store, userId);

        String userPassword =user.getPassword();

        if (!passwordEncoder.matches(password, userPassword)) {
            throw new BaseException(ErrorCode.INVALID_PASSWORD);
        }

        reviewCommentRepository.deleteById(reviewId);
    }

    public User getUserByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER_ID));
    }

    public Store getStoreByStoreId(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_STORE_ID));
    }

    public Review getReviewByReviewId(Long reviewId) {
        return reviewRepository.findByOrderId(reviewId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_REVIEW));
    }

    public ReviewComment getReviewCommentByReviewId(Long reviewId) {
        return reviewCommentRepository.findById(reviewId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_REVIEW_COMMENT));
    }

    public void validateMyStore(Store store, Long userId) {
        if (!store.getUser().getId().equals(userId)) {
            throw new BaseException(ErrorCode.UNAUTHORIZED_USER_ID);
        }
    }
}
