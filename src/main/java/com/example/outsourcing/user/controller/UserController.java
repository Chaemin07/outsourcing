package com.example.outsourcing.user.controller;

import com.example.outsourcing.common.annotation.AuthUser;
import com.example.outsourcing.common.response.ApiResponse;
import com.example.outsourcing.user.dto.PwdUpdateRequestDTO;
import com.example.outsourcing.user.dto.UserDeactiveRequestDTO;
import com.example.outsourcing.user.dto.UserResponseDTO;
import com.example.outsourcing.user.dto.UserSignupRequestDTO;
import com.example.outsourcing.user.dto.UserUpdateRequestDTO;
import com.example.outsourcing.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

  private final UserService userService;

  // TODO: userId 없애기

  // 회원 가입
  @PostMapping(value = "/signup")
  public ResponseEntity<ApiResponse<Void>> signup(
      @Valid @RequestBody UserSignupRequestDTO requestDTO) {
    userService.signup(requestDTO);
    return ResponseEntity.ok().build();
  }

  // 프로필 이미지 업로드
  @PostMapping("/users/profile")
  public ResponseEntity<Void> uploadProfile(@AuthUser Long userId,
      @RequestParam MultipartFile image) {
    userService.uploadProfile(userId, image);
    return ResponseEntity.ok().build();
  }

  // 회원 조회
  @GetMapping("/users/{userId}")
  public ResponseEntity<ApiResponse<UserResponseDTO>> getUser(@AuthUser Long userId) {
    return ResponseEntity.ok(
        ApiResponse.success(userService.getUser(userId)));
  }

  // 회원 정보 수정
  @PatchMapping("/users/{userId}")
  public ResponseEntity<ApiResponse<Void>> updateUser(@AuthUser Long userId,
      @RequestBody UserUpdateRequestDTO requestDTO) {
    userService.updateUser(userId, requestDTO);
    return ResponseEntity.ok(ApiResponse.success());
  }

  // 비밀번호 수정
  @PatchMapping("/users/{userId}/password")
  public ResponseEntity<ApiResponse<Void>> updatePassword(@AuthUser Long userId,
      @RequestBody PwdUpdateRequestDTO requestDTO) {
    userService.updatePassword(userId, requestDTO);
    return ResponseEntity.ok(ApiResponse.success());
  }

  // 회원 탈퇴
  @DeleteMapping("/users/{userId}")
  public ResponseEntity<ApiResponse<Void>> deactiveUser(@AuthUser Long userId,
      @RequestBody UserDeactiveRequestDTO requestDTO) {
    userService.deactiveUser(userId, requestDTO);
    return ResponseEntity.ok(ApiResponse.success());
  }

  // TODO : 유저 주소 추가 등록 API 작업
}
