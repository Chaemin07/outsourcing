package com.example.outsourcing.user.controller;

import com.example.outsourcing.address.dto.UpdateUserAddressRequestDTO;
import com.example.outsourcing.address.service.AddressService;
import com.example.outsourcing.common.annotation.AuthUser;
import com.example.outsourcing.common.response.ApiResponse;
import com.example.outsourcing.image.util.ImageUtil;
import com.example.outsourcing.user.dto.PwdUpdateRequestDTO;
import com.example.outsourcing.user.dto.UserDeactivateRequestDTO;
import com.example.outsourcing.user.dto.UserResponseDTO;
import com.example.outsourcing.user.dto.UserSignupRequestDTO;
import com.example.outsourcing.user.dto.UserUpdateRequestDTO;
import com.example.outsourcing.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  private final AddressService addressService;
  private final ImageUtil imageUtil;

  // 회원 가입
  @PostMapping(value = "/signup")
  public ResponseEntity<ApiResponse<Void>> signup(
      @Valid @RequestBody UserSignupRequestDTO requestDTO) {
    userService.signup(requestDTO);
    return ResponseEntity.ok().build();
  }

  // 프로필 이미지 업로드
  @PostMapping("/users/profile")
  public ResponseEntity<ApiResponse<Void>> uploadProfile(@AuthUser Long userId,
      @RequestParam MultipartFile image) {
    userService.uploadProfileImg(userId, image);
    return ResponseEntity.ok(ApiResponse.success());
  }

  // 프로필 이미지 조회
  @GetMapping("/users/profile")
  public ResponseEntity<Resource> getImage(@AuthUser Long userId) {
    return imageUtil.getImage(userService.getProfileImgId(userId));
  }

  // 회원 조회
  @GetMapping("/users")
  public ResponseEntity<ApiResponse<UserResponseDTO>> getUser(@AuthUser Long userId) {
    return ResponseEntity.ok(
        ApiResponse.success(userService.getUser(userId)));
  }

  // 회원 정보 수정
  @PatchMapping("/users")
  public ResponseEntity<ApiResponse<Void>> updateUser(@AuthUser Long userId,
      @RequestBody UserUpdateRequestDTO requestDTO) {
    userService.updateUser(userId, requestDTO);
    return ResponseEntity.ok(ApiResponse.success());
  }

  // 비밀번호 수정
  @PatchMapping("/users/password")
  public ResponseEntity<ApiResponse<Void>> updatePassword(@AuthUser Long userId,
      @RequestBody PwdUpdateRequestDTO requestDTO) {
    userService.updatePassword(userId, requestDTO);
    return ResponseEntity.ok(ApiResponse.success());
  }

  // 회원 탈퇴
  @DeleteMapping("/users")
  public ResponseEntity<ApiResponse<Void>> deactiveUser(@AuthUser Long userId,
      @RequestBody UserDeactivateRequestDTO requestDTO) {
    userService.deactivateUser(userId, requestDTO);
    return ResponseEntity.ok(ApiResponse.success());
  }

  // 회원 주소 추가
  @PostMapping("/addresses")
  public ResponseEntity<ApiResponse<Void>> createUserAddress(
      @AuthUser Long userId, @RequestBody UpdateUserAddressRequestDTO requestDTO) {
    addressService.createAddress(userId, requestDTO);
    return ResponseEntity.ok(ApiResponse.success());
  }

  // 회원 주소 수정
  @PatchMapping("/users/addresses/{addressId}")
  public ResponseEntity<ApiResponse<Void>> updateUserAddress(
      @AuthUser Long userId, @PathVariable Long addressId,
      @RequestBody UpdateUserAddressRequestDTO requestDTO) {
    addressService.updateAddress(userId, addressId, requestDTO);
    return ResponseEntity.ok(ApiResponse.success());
  }

  // 대표 주소 설정
  @PatchMapping("/users/addresses/{addressId}/default")
  public ResponseEntity<ApiResponse<Void>> setUserDefaultAddress(
      @AuthUser Long userId, @PathVariable Long addressId) {
    addressService.setDefaultAddress(userId, addressId);
    return ResponseEntity.ok(ApiResponse.success());
  }

  // 회원 주소 삭제
  @DeleteMapping("/users/addresses/{addressId}")
  public ResponseEntity<ApiResponse<Void>> deleteUserAddress(
      @AuthUser Long userId, @PathVariable Long addressId) {
    addressService.deleteAddress(userId, addressId);
    return ResponseEntity.ok(ApiResponse.success());
  }
}
