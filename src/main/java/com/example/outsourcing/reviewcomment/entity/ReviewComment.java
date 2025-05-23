package com.example.outsourcing.reviewcomment.entity;

import com.example.outsourcing.common.entity.BaseEntity;
import com.example.outsourcing.review.entity.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "review_comment")
public class ReviewComment extends BaseEntity {

    @Id
    private Long id;

    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Review review;

    public ReviewComment(String content, Review review) {
        this.content = content;
        this.review = review;
    }

    public void updateReviewComment(String content) {
        this.content = content;
    }
}
