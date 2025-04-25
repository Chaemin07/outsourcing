package com.example.outsourcing.review.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

@Getter
@AllArgsConstructor
public class ReviewRequestDto {

    @Size(max = 100, message = "리뷰 내용은 100자 이하로 작성해야 합니다.")
    private final String content;

    @Range(min = 1, max = 5)
    @NotNull
    private final int score;
    
    // 사진 추가
}
