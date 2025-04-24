package com.example.outsourcing.review.controller;

import com.example.outsourcing.review.dto.request.DeleteReviewRequestDto;
import com.example.outsourcing.review.dto.request.ReviewRequestDto;
import com.example.outsourcing.review.dto.response.ReviewResponseDto;
import com.example.outsourcing.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders/{orderId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 생성
    @PostMapping
    public ResponseEntity<ReviewResponseDto> saveReview(@PathVariable Long orderId, @Valid @RequestBody ReviewRequestDto dto) {
        // 로그인 나오면 HttpServletRequest

        Long userId = 1L;

        ReviewResponseDto reviewResponseDto = reviewService.saveReview(userId, orderId, dto);

        return new ResponseEntity<>(reviewResponseDto,HttpStatus.CREATED);
    }
    
    // 리뷰 조회
    
    // 리뷰 수정
    @PatchMapping
    public ResponseEntity<ReviewResponseDto> updateReview(@PathVariable Long orderId, @Valid @RequestBody ReviewRequestDto dto) {
        // 로그인 나오면 HttpServletRequest

        Long userId = 1L;

        ReviewResponseDto reviewResponseDto = reviewService.updateReview(userId, orderId, dto);

        return new ResponseEntity<>(reviewResponseDto, HttpStatus.OK);
    }
    
    // 리뷰 삭제
    @DeleteMapping
    public ResponseEntity<String> deleteReview(@PathVariable Long orderId, @Valid @RequestBody DeleteReviewRequestDto dto) {
        // 로그인 나오면 HttpServletRequest

        long userId = 1L;

        reviewService.deleteReview(userId, orderId, dto.getPassword());

        return new ResponseEntity<>("리뷰 삭제가 완료되었습니다.", HttpStatus.OK);
    }
}
