package com.example.outsourcing.review.controller;

import com.example.outsourcing.review.dto.request.ReviewRequestDto;
import com.example.outsourcing.review.dto.response.ReviewResponseDto;
import com.example.outsourcing.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores/{storeId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDto> saveReview(@PathVariable Long storeId, @Valid @RequestBody ReviewRequestDto dto) {
        // 로그인 나오면 HttpServletRequest

        long userId = 1L; // 로그인된 유저 id 값 가져오기
        long orderId = dto.getOrder_id();

        ReviewResponseDto reviewResponseDto = reviewService.saveReview(storeId, userId, orderId, dto);

        return new ResponseEntity<>(reviewResponseDto,HttpStatus.CREATED);
    }
}
