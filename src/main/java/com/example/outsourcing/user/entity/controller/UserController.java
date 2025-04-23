package com.example.outsourcing.user.entity.controller;

import com.example.outsourcing.user.entity.dto.UserSignupRequestDTO;
import com.example.outsourcing.user.entity.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  // 회원 가입
  @PostMapping("/signup")
  public ResponseEntity<Void> signup (@Valid @RequestBody UserSignupRequestDTO requestDTO) {
    userService.signup(requestDTO);
    return ResponseEntity.ok().build();
  }

  // 회원 조회

  // 회원 정보 수정

  // 비밀번호 수정

  // 회원 탈퇴
}
