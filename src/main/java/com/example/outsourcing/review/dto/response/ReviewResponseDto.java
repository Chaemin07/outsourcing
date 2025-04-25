package com.example.outsourcing.review.dto.response;

import com.example.outsourcing.order.entity.Order;
import com.example.outsourcing.review.entity.Review;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewResponseDto {

    // 리뷰 식별자
    private final long id;

    // 리뷰 댓글
    private final String content;

    // 리뷰 점수
    private final int score;

    // 이미지 주소
    // private final

    // 리뷰 작성자 id
    private final long userId;

    // 리뷰를 작성하는 주문 id
    private final long orderId;

    // 리뷰를 작성하는 가게 id
    private final long storeId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String commentContent;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.content = review.getContent();
        this.score = review.getScore();

        Order order = review.getOrder();
        this.orderId = order.getId();
        this.userId = order.getUserId();
        this.storeId = order.getStoreId();
        this.commentContent = review.getReviewComment() != null ? review.getReviewComment().getContent() : null;
    }
}
