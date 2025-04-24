package com.example.outsourcing.user.entity.controller;

import com.example.outsourcing.user.entity.dto.PwdUpdateRequestDTO;
import com.example.outsourcing.user.entity.dto.UserResponseDTO;
import com.example.outsourcing.user.entity.dto.UserSignupRequestDTO;
import com.example.outsourcing.user.entity.dto.UserUpdateRequestDTO;
import com.example.outsourcing.user.entity.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  // 회원 가입
  @PostMapping("/signup")
  public ResponseEntity<Void> signup(@Valid @RequestBody UserSignupRequestDTO requestDTO) {
    userService.signup(requestDTO);
    return ResponseEntity.ok().build();
  }

  // 회원 조회
  @GetMapping("/users/{userId}")
  public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long userId) {
    return ResponseEntity.ok(userService.getUser(userId));    // TODO: 토큰, 세션에서 가져오는 방식으로 변경
  }

  // 회원 정보 수정
  @PatchMapping("/users/{userId}")
  public ResponseEntity<Void> updateUser(@PathVariable Long userId,
      @RequestBody UserUpdateRequestDTO requestDTO) {
    userService.updateUser(userId, requestDTO);
    return ResponseEntity.ok().build();
  }

  // 비밀번호 수정
  @PatchMapping("/users/{userId}/password")
  public ResponseEntity<Void> updatePassword(@PathVariable Long userId,
      @RequestBody PwdUpdateRequestDTO requestDTO) {
    userService.updatePassword(userId, requestDTO);
    return ResponseEntity.ok().build();
  }

  // 회원 탈퇴
}
