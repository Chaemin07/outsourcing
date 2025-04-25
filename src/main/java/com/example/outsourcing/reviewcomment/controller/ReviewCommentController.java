package com.example.outsourcing.reviewcomment.controller;

import com.example.outsourcing.common.annotation.AuthUser;
import com.example.outsourcing.review.dto.request.DeleteReviewRequestDto;
import com.example.outsourcing.reviewcomment.dto.request.ReviewCommentRequestDto;
import com.example.outsourcing.reviewcomment.dto.response.ReviewCommentResponseDto;
import com.example.outsourcing.reviewcomment.service.ReviewCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews/{reviewId}/comments")
public class ReviewCommentController {

    private final ReviewCommentService reviewCommentService;
    
    // 리뷰 댓글 생성
    @PostMapping
    public ResponseEntity<ReviewCommentResponseDto> saveReviewComment(@PathVariable Long reviewId, @Valid @RequestBody ReviewCommentRequestDto dto, @AuthUser Long userId) {

        ReviewCommentResponseDto reviewCommentResponseDto = reviewCommentService.saveReviewComment(userId, reviewId, dto);

        return new ResponseEntity<>(reviewCommentResponseDto, HttpStatus.CREATED);
    }
    
    // 리뷰 댓글 수정
    @PatchMapping
    public ResponseEntity<ReviewCommentResponseDto> updateReviewComment(@PathVariable Long reviewId, @Valid @RequestBody ReviewCommentRequestDto dto, @AuthUser Long userId) {

        ReviewCommentResponseDto reviewCommentResponseDto = reviewCommentService.updateReviewComment(userId, reviewId, dto);

        return new ResponseEntity<>(reviewCommentResponseDto, HttpStatus.OK);
    }
    
    // 리뷰 댓글 삭제
    @DeleteMapping
    public ResponseEntity<String> deleteReviewComment(@PathVariable Long reviewId, @Valid @RequestBody DeleteReviewRequestDto dto, @AuthUser Long userId) {

        reviewCommentService.deleteReviewComment(userId, reviewId, dto.getPassword());

        return new ResponseEntity<>("리뷰 댓글 삭제가 완료되었습니다.", HttpStatus.OK);
    }
}
