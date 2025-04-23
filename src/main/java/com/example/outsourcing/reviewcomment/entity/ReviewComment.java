package com.example.outsourcing.reviewcomment.entity;

import com.example.outsourcing.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reviewComment")
public class ReviewComment extends BaseEntity {

    @Id
    private Long id;

    private String content;
}
