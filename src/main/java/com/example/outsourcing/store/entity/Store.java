package com.example.outsourcing.store.entity;

import com.example.outsourcing.address.entity.Address;
import com.example.outsourcing.common.entity.BaseEntity;
import com.example.outsourcing.image.entity.Image;
import com.example.outsourcing.menu.entity.Menu;
import com.example.outsourcing.store.dto.request.CreateStoreRequestDto;
import com.example.outsourcing.store.dto.request.UpdateStoreRequestDto;
import com.example.outsourcing.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private String status;

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

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne
    private User user;

    @OneToOne
    private Address address;

    @ManyToOne
    private Image image;

    public Store(CreateStoreRequestDto requestDto) {
        this.name = requestDto.getName();
        this.status = requestDto.getStatus();
        this.storePhoneNumber = requestDto.getStorePhoneNumber();
        this.minOrderPrice = requestDto.getMinOrderPrice();
        this.openingTimes = requestDto.getOpeningTimes();
        this.closingTimes = requestDto.getClosingTimes();
        this.notification = requestDto.getNotification();
    }

    public void updateStore(UpdateStoreRequestDto requestDto) {
        this.name = requestDto.getName();
        this.status = requestDto.getStatus();
        this.storePhoneNumber = requestDto.getStorePhoneNumber();
        this.minOrderPrice = requestDto.getMinOrderPrice();
        this.openingTimes = requestDto.getOpeningTimes();
        this.closingTimes = requestDto.getClosingTimes();
        this.notification = requestDto.getNotification();
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

}
