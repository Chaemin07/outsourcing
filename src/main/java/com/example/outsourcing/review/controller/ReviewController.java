package com.example.outsourcing.review.controller;

import com.example.outsourcing.common.annotation.AuthUser;
import com.example.outsourcing.common.enums.SortType;
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
    public ResponseEntity<ReviewResponseDto> saveReview(@PathVariable Long orderId, @Valid @RequestBody ReviewRequestDto dto, @AuthUser Long userId) {

        ReviewResponseDto reviewResponseDto = reviewService.saveReview(userId, orderId, dto);

        return new ResponseEntity<>(reviewResponseDto,HttpStatus.CREATED);
    }
    
    // 리뷰 조회
    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<ReviewListResponseDto> getStoreReviews(@PathVariable Long storeId, @RequestParam(defaultValue = "latest") String sort) {

        SortType sortType = SortType.from(sort);
        ReviewListResponseDto reviewsByStoreId = reviewService.getReviewsByStoreId(storeId, sortType);

        return new ResponseEntity<>(reviewsByStoreId, HttpStatus.OK);
    }
    
    // 리뷰 수정
    @PatchMapping("/orders/{orderId}/reviews")
    public ResponseEntity<ReviewResponseDto> updateReview(@PathVariable Long orderId, @Valid @RequestBody ReviewRequestDto dto, @AuthUser Long userId) {

        ReviewResponseDto reviewResponseDto = reviewService.updateReview(userId, orderId, dto);

        return new ResponseEntity<>(reviewResponseDto, HttpStatus.OK);
    }
    
    // 리뷰 삭제
    @DeleteMapping("/orders/{orderId}/reviews")
    public ResponseEntity<String> deleteReview(@PathVariable Long orderId, @Valid @RequestBody DeleteReviewRequestDto dto, @AuthUser Long userId) {

        reviewService.deleteReview(userId, orderId, dto.getPassword());

        return new ResponseEntity<>("리뷰 삭제가 완료되었습니다.", HttpStatus.OK);
    }
}
