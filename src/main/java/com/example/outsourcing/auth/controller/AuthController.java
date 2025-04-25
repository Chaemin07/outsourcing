package com.example.outsourcing.auth.controller;

import com.example.outsourcing.auth.dto.LoginResponseDTO;
import com.example.outsourcing.auth.service.AuthService;
import com.example.outsourcing.common.response.ApiResponse;
import com.example.outsourcing.user.dto.userLoginRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public LoginResponseDTO login(
      @Valid @RequestBody userLoginRequestDTO requestDTO) {
    return authService.login(requestDTO);
  }

  @PostMapping("/logout")
  public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {

    return ResponseEntity.ok().build();
  }
}
