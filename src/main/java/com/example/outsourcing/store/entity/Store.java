package com.example.outsourcing.store.entity;

import com.example.outsourcing.Image.entity.Image;
import com.example.outsourcing.address.entity.Address;
import com.example.outsourcing.common.entity.BaseEntity;
import com.example.outsourcing.user.entity.User;
import jakarta.persistence.Entity;
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

    private String name;

    private String status;

    private String phone;

    private String storePhoneNumber;

    private Integer minOderPrice;

    private String openingTimes;

    private String closingTimes;

    private String notification;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    private Address address;

    @OneToOne
    private Image image;


}
