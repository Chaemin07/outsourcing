package com.example.outsourcing.reviewcomment.repository;

import com.example.outsourcing.reviewcomment.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {
}
