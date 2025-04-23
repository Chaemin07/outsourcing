package com.example.outsourcing.user.entity;

import com.example.outsourcing.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(nullable = false)
  String name;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  Role role;

  @Column(nullable = false)
  String nickname;

  @Column(nullable = false, unique = true)
  String email;

  @Column(nullable = false)
  String password;

  @Enumerated(EnumType.STRING)
  OAuth oAuth;

  @Column(nullable = false)
  String phone;
}
