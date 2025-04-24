package com.example.outsourcing.review.controller;

import com.example.outsourcing.review.dto.request.DeleteReviewRequestDto;
import com.example.outsourcing.review.dto.request.ReviewRequestDto;
import com.example.outsourcing.review.dto.response.ReviewListResponseDto;
import com.example.outsourcing.review.dto.response.ReviewResponseDto;
import com.example.outsourcing.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 생성
    @PostMapping("/orders/{orderId}/reviews")
    public ResponseEntity<ReviewResponseDto> saveReview(@PathVariable Long orderId, @Valid @RequestBody ReviewRequestDto dto) {
        // 로그인 나오면 HttpServletRequest

        Long userId = 1L;

        ReviewResponseDto reviewResponseDto = reviewService.saveReview(userId, orderId, dto);

        return new ResponseEntity<>(reviewResponseDto,HttpStatus.CREATED);
    }
    
    // 리뷰 조회
    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<ReviewListResponseDto> getStoreReviews(@PathVariable Long storeId) {

        // 정렬을 어떻게 받아올건지 고민.
        // param?
        // body?
        // 기본 - 최신순
        // + 별점 높은 순 => score desc
        // + 별점 낮은 순 => score asc
        // 사진 추가 후 -> 사진이 있는 => img 경로가 NULL 값이 아닌

        ReviewListResponseDto reviewsByStoreId = reviewService.getReviewsByStoreId(storeId);

        return new ResponseEntity<>(reviewsByStoreId, HttpStatus.OK);
    }
    
    // 리뷰 수정
    @PatchMapping("/orders/{orderId}/reviews")
    public ResponseEntity<ReviewResponseDto> updateReview(@PathVariable Long orderId, @Valid @RequestBody ReviewRequestDto dto) {
        // 로그인 나오면 HttpServletRequest

        Long userId = 1L;

        ReviewResponseDto reviewResponseDto = reviewService.updateReview(userId, orderId, dto);

        return new ResponseEntity<>(reviewResponseDto, HttpStatus.OK);
    }
    
    // 리뷰 삭제
    @DeleteMapping("/orders/{orderId}/reviews")
    public ResponseEntity<String> deleteReview(@PathVariable Long orderId, @Valid @RequestBody DeleteReviewRequestDto dto) {
        // 로그인 나오면 HttpServletRequest

        long userId = 1L;

        reviewService.deleteReview(userId, orderId, dto.getPassword());

        return new ResponseEntity<>("리뷰 삭제가 완료되었습니다.", HttpStatus.OK);
    }
}
