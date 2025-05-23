package com.example.outsourcing.user.dto;

import com.example.outsourcing.address.entity.Address;
import com.example.outsourcing.user.entity.Role;
import com.example.outsourcing.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class UserResponseDTO {

  private String name;
  private String email;
  private String phoneNumber;
  private List<String> addresses;
  private String nickname;
  private Role role;

  public UserResponseDTO(User user) {
    this.name = user.getName();
    this.email = user.getEmail();
    this.phoneNumber = user.getPhoneNumber();
    this.addresses = user.getAddresses().stream()
        .map(Address::getAddress)
        .collect(Collectors.toList());
    this.nickname = user.getNickname();
    this.role = user.getRole();
  }
}
