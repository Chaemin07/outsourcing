package com.example.outsourcing.user.controller;

import com.example.outsourcing.common.annotation.AuthUser;
import com.example.outsourcing.common.response.ApiResponse;
import com.example.outsourcing.user.dto.PwdUpdateRequestDTO;
import com.example.outsourcing.user.dto.UserDeactiveRequestDTO;
import com.example.outsourcing.user.dto.UserResponseDTO;
import com.example.outsourcing.user.dto.UserSignupRequestDTO;
import com.example.outsourcing.user.dto.UserUpdateRequestDTO;
import com.example.outsourcing.user.dto.userLoginRequestDTO;
import com.example.outsourcing.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
  public ResponseEntity<ApiResponse<Void>> signup(
      @Valid @RequestBody UserSignupRequestDTO requestDTO) {
    userService.signup(requestDTO);
    return ResponseEntity.ok().build();
  }

  // TODO : 로그인, 로그아웃, 유저 주소 추가 등록 API 작업
  @PostMapping("/login")
  public ResponseEntity<ApiResponse<Void>> login(
      @Valid @RequestBody userLoginRequestDTO requestDTO) {
    userService.login(requestDTO);
    return ResponseEntity.ok().build();
  }

  // 회원 조회
  @GetMapping("/users/{userId}")
  public ResponseEntity<ApiResponse<UserResponseDTO>> getUser(@AuthUser Long userId) {
    return ResponseEntity.ok(
        ApiResponse.success(userService.getUser(userId)));    // TODO: 토큰, 세션에서 가져오는 방식으로 변경
  }

  // 회원 정보 수정
  @PatchMapping("/users/{userId}")
  public ResponseEntity<ApiResponse<Void>> updateUser(@PathVariable Long userId,
      @RequestBody UserUpdateRequestDTO requestDTO) {
    userService.updateUser(userId, requestDTO);
    return ResponseEntity.ok(ApiResponse.success());
  }

  // 비밀번호 수정
  @PatchMapping("/users/{userId}/password")
  public ResponseEntity<ApiResponse<Void>> updatePassword(@PathVariable Long userId,
      @RequestBody PwdUpdateRequestDTO requestDTO) {
    userService.updatePassword(userId, requestDTO);
    return ResponseEntity.ok(ApiResponse.success());
  }

  // 회원 탈퇴
  @DeleteMapping("/users/{userId}")
  public ResponseEntity<ApiResponse<Void>> deactiveUser(@PathVariable Long userId,
      @RequestBody UserDeactiveRequestDTO requestDTO) {
    userService.deactiveUser(userId, requestDTO);
    return ResponseEntity.ok(ApiResponse.success());
  }
}
