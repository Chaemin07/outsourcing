package com.example.outsourcing.menu.entity;

import com.example.outsourcing.cart.entity.Cart;
import com.example.outsourcing.common.entity.BaseEntity;
import com.example.outsourcing.image.entity.Image;
import com.example.outsourcing.store.entity.Store;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

  // 상태는 enum으로 구현하는게 좋을거 같은데요??
  private String status;

  private LocalDateTime deletedAt;

  @ManyToOne
  @JoinColumn(name = "store_id")
  private Store store;

  @OneToOne
  private Image image;

  @OneToMany(mappedBy = "menu")
  private List<Cart> cartList = new ArrayList<>();

  @OneToMany(mappedBy = "menu", orphanRemoval = true, fetch = FetchType.LAZY)
  private List<MenuOption> options = new ArrayList<>();
}
