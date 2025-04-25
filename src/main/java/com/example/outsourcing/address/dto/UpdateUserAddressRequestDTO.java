package com.example.outsourcing.address.dto;

import com.example.outsourcing.user.entity.Role;
import lombok.Getter;

@Getter
public class UpdateUserAddressRequestDTO {

  private final Role role = Role.USER;
  private String address;
  private boolean isDefault;
}
