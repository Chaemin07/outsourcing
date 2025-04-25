package com.example.outsourcing.common.dto;

import com.example.outsourcing.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthSession {

  private final Long userId;
  private final String email;
  private final Role role;
}
