package com.example.outsourcing.menu.entity;


import com.example.outsourcing.common.entity.BaseEntity;
import com.example.outsourcing.menu.dto.request.AddMenuOptionRequestDto;
import com.example.outsourcing.order.entity.OrderItemOption;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "menu_option")
public class MenuOption extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(nullable = false)
    private String optionName;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "menuOption")
    private List<OrderItemOption> orderItemOptionList = new ArrayList<>();

    public MenuOption(Menu menu, AddMenuOptionRequestDto requestDto) {
        this.menu = menu;
        this.optionName = requestDto.getOptionName();
        this.price = requestDto.getPrice();
        this.status = Status.ACTIVE; // 기본 상태를 ACTIVE로 생성
    }

    public void updateOption(String optionName, Integer price) {
        this.optionName = optionName;
        this.price = price;
    }

    public void softDelete() {
        this.status = Status.INACTIVE;
        this.deletedAt = LocalDateTime.now();  // BaseEntity의 delete() 호출 (deletedAt 세팅)
    }

}
