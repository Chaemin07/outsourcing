package com.example.outsourcing.reviewcomment.entity;

import com.example.outsourcing.common.entity.BaseEntity;
import com.example.outsourcing.review.entity.Review;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "review_comment")
public class ReviewComment extends BaseEntity {

    @Id
    private Long id;

    private String content;

    @OneToOne
    @MapsId
    private Review review;
}
