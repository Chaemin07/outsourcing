package com.example.outsourcing.reviewcomment.entity;

import com.example.outsourcing.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "review_comment")
public class ReviewComment extends BaseEntity {

    @Id
    private Long id;

    private String content;
}
