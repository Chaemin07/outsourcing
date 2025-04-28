package com.example.outsourcing.menu.entity;

import com.example.outsourcing.cart.entity.Cart;
import com.example.outsourcing.common.entity.BaseEntity;
import com.example.outsourcing.image.entity.Image;
import com.example.outsourcing.menu.dto.request.AddMenuRequestDto;
import com.example.outsourcing.menu.dto.request.UpdateMenuRequestDto;
import com.example.outsourcing.order.entity.OrderDetail;
import com.example.outsourcing.order.entity.OrderItemOption;
import com.example.outsourcing.store.entity.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "menu")
public class Menu extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Integer price;

  @Column(nullable = false)
  private String descrption;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Status status = Status.ACTIVE;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id")
  private Store store;

  @Setter
  @OneToOne
  private Image image;

  @OneToMany(mappedBy = "menu")
  private List<Cart> cartList = new ArrayList<>();

  // 사장설정 메뉴 옵션
  @OneToMany(mappedBy = "menu", orphanRemoval = true, fetch = FetchType.LAZY)
  private List<MenuOption> options = new ArrayList<>();

  // 주문 상세
  @OneToMany(mappedBy = "menu", orphanRemoval = true, fetch = FetchType.LAZY)
  private List<OrderDetail> orderDetailList = new ArrayList<>();

  // 주문 항목 옵션
  @OneToMany(mappedBy = "menu")
  private List<OrderItemOption> orderItemOptionList = new ArrayList<>();

  public Menu(Store store, AddMenuRequestDto requestDto) {

    this.store = store;
    this.name = requestDto.getName();
    this.price = requestDto.getPrice();
    this.descrption = requestDto.getDescrption();
    this.status = Status.ACTIVE;
  }

  public void updateMenu(UpdateMenuRequestDto requestDto) {
    this.name = requestDto.getName();
    this.price = requestDto.getPrice();
    this.descrption = requestDto.getDescrption();
    this.status = Status.ACTIVE;
  }

  public void validateStore(Long storeId) {
    if (!this.store.getId().equals(storeId)) {
      throw new RuntimeException("가게 소유주가 아닙니다.");
    }
  }

  public void softDelete() {
    this.deletedAt = LocalDateTime.now();
    this.status = Status.DELETED;
  }

  // 삭제 처리 확인 메서드
  public boolean isDeleted() {
    return deletedAt != null;
  }

}
