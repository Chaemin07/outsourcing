package com.example.outsourcing.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewListResponseDto {

    private final Long countReviews;

    private final double averageScore;

    private List<ReviewResponseDto> reviewsList;
}
