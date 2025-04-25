package com.example.outsourcing.store.entity;

import com.example.outsourcing.address.entity.Address;
import com.example.outsourcing.common.entity.BaseEntity;
import com.example.outsourcing.image.entity.Image;
import com.example.outsourcing.store.dto.request.CreateStoreRequestDto;
import com.example.outsourcing.store.dto.request.UpdateStoreRequestDto;
import com.example.outsourcing.user.entity.User;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "store")
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    @Column(nullable = false)
    private String storePhoneNumber;

    @Column(nullable = false)
    private Integer minOrderPrice;

    @Column(nullable = false)
    private String openingTimes;

    @Column(nullable = false)
    private String closingTimes;

    @Column(nullable = false)
    private String notification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    public Store(CreateStoreRequestDto requestDto, User user) {
        this.name = requestDto.getName();
        this.status = StoreStatus.OPEN;
        this.storePhoneNumber = requestDto.getStorePhoneNumber();
        this.minOrderPrice = requestDto.getMinOrderPrice();
        this.openingTimes = requestDto.getOpeningTimes();
        this.closingTimes = requestDto.getClosingTimes();
        this.notification = requestDto.getNotification();
        this.user = user;
    }

    public void updateStore(UpdateStoreRequestDto requestDto) {
        this.name = requestDto.getName();
        this.storePhoneNumber = requestDto.getStorePhoneNumber();
        this.minOrderPrice = requestDto.getMinOrderPrice();
        this.openingTimes = requestDto.getOpeningTimes();
        this.closingTimes = requestDto.getClosingTimes();
        this.notification = requestDto.getNotification();
    }

    public void closeDown() {
        this.status = StoreStatus.CLOSED_DOWN;
    }
}
