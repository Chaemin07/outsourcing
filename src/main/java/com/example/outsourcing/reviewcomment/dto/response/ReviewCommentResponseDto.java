package com.example.outsourcing.reviewcomment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReviewCommentResponseDto {

    private final Long reviewId;

    private final String content;

}
