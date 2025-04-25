package com.example.outsourcing.reviewcomment.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReviewCommentRequestDto {

    @Size(max = 100, message = "리뷰 내용은 100자 이하로 작성해야 합니다.")
    private final String content;
}
