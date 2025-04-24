package com.example.outsourcing.store.entity;

import com.example.outsourcing.address.entity.Address;
import com.example.outsourcing.common.entity.BaseEntity;
import com.example.outsourcing.image.entity.Image;
import com.example.outsourcing.menu.entity.Menu;
import com.example.outsourcing.store.dto.request.CreateStoreRequestDto;
import com.example.outsourcing.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

    private String name;

    private String status;

    private String storePhoneNumber;

    private Integer minOderPrice;

    private String openingTimes;

    private String closingTimes;

    private String notification;

    @ManyToOne
    private User user;

    @OneToOne
    private Address address;

    @ManyToOne
    private Image image;

    public Store(String name, String status, String storePhoneNumber,
        Integer minOderPrice, String openingTimes, String closingTimes, String notification
       ) {
        this.name = name;
        this.status = status;
        this.storePhoneNumber = storePhoneNumber;
        this.minOderPrice = minOderPrice;
        this.openingTimes = openingTimes;
        this.closingTimes = closingTimes;
        this.notification = notification;
    }

    public void updateStore(String name, String status, String storePhoneNumber,
        Integer minOderPrice, String openingTimes, String closingTimes, String notification) {
        this.name = name;
        this.status = status;
        this.storePhoneNumber = storePhoneNumber;
        this.minOderPrice = minOderPrice;
        this.openingTimes = openingTimes;
        this.closingTimes = closingTimes;
        this.notification = notification;
    }

}
