package com.example.outsourcing.address.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateUserAddressRequestDTO {

  @NotBlank
  private String address;
  private boolean isDefault;
}
