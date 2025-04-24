package com.example.outsourcing.address.entity;

import com.example.outsourcing.common.entity.BaseEntity;
import com.example.outsourcing.user.entity.Role;
import com.example.outsourcing.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "address")
public class Address extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(nullable = false)
  String address;

  @Column(nullable = false)
  boolean isDefault;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  Role role;

  @ManyToOne
  @JoinColumn(name = "user_id")
  User user;
}
