package com.example.outsourcing.review.entity;

import com.example.outsourcing.common.entity.BaseEntity;
import com.example.outsourcing.image.entity.Image;
import com.example.outsourcing.order.entity.Order;
import com.example.outsourcing.reviewcomment.entity.ReviewComment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "review")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Column(nullable = false)
    private int score;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private Long storeId;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @OneToOne(mappedBy = "review", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ReviewComment reviewComment;
    
    // 이미지 연결

    public Review(String content, int score, Long storeId, Order order) {
        this.content = content;
        this.score = score;
        this.storeId = storeId;
        this.order = order;
    }

    public void updateReview(String content, int score) {
        this.content = content;
        this.score = score;
    }
}
