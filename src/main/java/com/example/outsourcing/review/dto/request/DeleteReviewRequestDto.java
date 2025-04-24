package com.example.outsourcing.review.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteReviewRequestDto { // 다른 곳에 비번 인증 Dto 있으면 가져와서 사용할지도..?

    // 비밀번호 정규 표현식 추가
    private final String password;
}
