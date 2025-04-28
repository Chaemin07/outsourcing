package com.example.outsourcing.address.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserAddressRequestDTO {

  @NotBlank
  private String address;
  private boolean isDefault;
}
