package com.example.outsourcing.reviewcomment.service;

import com.example.outsourcing.review.entity.Review;
import com.example.outsourcing.review.repository.ReviewRepository;
import com.example.outsourcing.reviewcomment.dto.request.ReviewCommentRequestDto;
import com.example.outsourcing.reviewcomment.dto.response.ReviewCommentResponseDto;
import com.example.outsourcing.reviewcomment.entity.ReviewComment;
import com.example.outsourcing.reviewcomment.repository.ReviewCommentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewCommentService {

    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;

    public ReviewCommentResponseDto saveReviewComment(Long userId, Long reviewId, @Valid ReviewCommentRequestDto dto) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰 없음"));

        // TODO: OWNER 사용자가 맞는지 확인
        // TODO: 본인 가게가 맞는 지 확인

        ReviewComment savedReviewComment = new ReviewComment(dto.getContent(), review);

        ReviewComment saved = reviewCommentRepository.save(savedReviewComment);

        return new ReviewCommentResponseDto(reviewId, savedReviewComment.getContent());
    }

    @Transactional
    public ReviewCommentResponseDto updateReviewComment(Long userId, Long reviewId, ReviewCommentRequestDto dto) {

        // TODO: 본인 댓글인지 확인

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰 없음"));

        ReviewComment reviewComment = reviewCommentRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰 댓글 없음"));

        reviewComment.updateReviewComment(dto.getContent());

        return new ReviewCommentResponseDto(reviewComment.getId(), reviewComment.getContent());
    }

    public void deleteReviewComment(Long userId, Long reviewId, String password) {

        // TODO: 본인 댓글인지 확인

        String userPassword = "1234";

        // TODO: Bcrypt => 비밀번호 matches로 검증
        if (!password.equals(userPassword)) {

            // 예외처리
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰 없음"));

        ReviewComment reviewComment = reviewCommentRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰 댓글 없음"));

        reviewCommentRepository.deleteById(reviewId);
    }
}
