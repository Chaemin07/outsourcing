package com.example.outsourcing.address.entity;

import com.example.outsourcing.address.dto.UpdateUserAddressRequestDTO;
import com.example.outsourcing.common.entity.BaseEntity;
import com.example.outsourcing.store.entity.Store;
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
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "address")
public class Address extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Setter
  @Column(nullable = false)
  String address;

  @Column(nullable = false)
  boolean isDefault = false;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  Role role;

  @ManyToOne
  @JoinColumn(name = "user_id")
  User user;

  @ManyToOne
  @JoinColumn(name = "store_id")
  Store store;

  public Address(UpdateUserAddressRequestDTO requestDTO, User user) {
    this.role = Role.USER;
    this.address = requestDTO.getAddress();
    this.user = user;
  }

  public Address(String address, Store store) {
    this.role = Role.OWNER;
    this.address = address;
    this.store = store;
  }

  // TODO: 접근 제어
  public void setDefault(boolean isDefault) {
    this.isDefault = isDefault;
  }
}
