package com.example.outsourcing.review.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

@Getter
@AllArgsConstructor
public class ReviewRequestDto {

    private final String content;

    @Range(min = 1, max = 5)
    @NotNull
    private final int score;
    
    // 사진 추가

    // 주문 생성 전 테스트용
    private final Long order_id;
}
