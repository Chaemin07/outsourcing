package com.example.outsourcing.menu.entity;

import com.example.outsourcing.common.entity.BaseEntity;
import com.example.outsourcing.image.entity.Image;
import com.example.outsourcing.store.entity.Store;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "menu")
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer price;

    private String status;

    private LocalDateTime deletedAt;

    @ManyToOne
    private Store store;

    @ManyToOne
    private Image image;


}
