package com.example.outsourcing.user.service;

import static com.example.outsourcing.common.exception.ErrorCode.CONFLICT_EMAIL;
import static com.example.outsourcing.common.exception.ErrorCode.DEACTIVATED_USER;
import static com.example.outsourcing.common.exception.ErrorCode.FAILED_UPLOAD_IMAGE;
import static com.example.outsourcing.common.exception.ErrorCode.INVALID_PASSWORD;
import static com.example.outsourcing.common.exception.ErrorCode.NOT_FOUND_USER_ID;

import com.example.outsourcing.address.dto.UpdateUserAddressRequestDTO;
import com.example.outsourcing.address.service.AddressService;
import com.example.outsourcing.common.config.PasswordEncoder;
import com.example.outsourcing.common.exception.BaseException;
import com.example.outsourcing.image.service.ImageService;
import com.example.outsourcing.user.dto.PwdUpdateRequestDTO;
import com.example.outsourcing.user.dto.UserDeactivateRequestDTO;
import com.example.outsourcing.user.dto.UserResponseDTO;
import com.example.outsourcing.user.dto.UserSignupRequestDTO;
import com.example.outsourcing.user.dto.UserUpdateRequestDTO;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final ImageService imageService;
  private final AddressService addressService;
  private final PasswordEncoder passwordEncoder;

  // 회원 가입
  @Transactional
  public void signup(UserSignupRequestDTO requestDTO) {
    // 유저 이메일(아이디) 중복 검사
    if (isExistsEmail(requestDTO.getEmail())) {
      throw new BaseException(CONFLICT_EMAIL);
    }

    // 비밀번호 인코딩
    String encodedPwd = passwordEncoder.encode(requestDTO.getPassword());
    requestDTO.setPassword(encodedPwd);

    // 저장
    User user = new User(requestDTO);
    userRepository.save(user);  // TODO: Mapper or MapStruct 로 DTO-엔티티 변환 구현

    // 주소 저장
    if (requestDTO.getAddress() != null) {
      UpdateUserAddressRequestDTO addressRequestDTO = new UpdateUserAddressRequestDTO(
          requestDTO.getAddress(), true);
      addressService.createAddress(user.getId(), addressRequestDTO);
    }
  }

  // 회원 조회
  public UserResponseDTO getUser(Long userId) {
    // 유효한 id 인지 검사
    User user = userRepository.findById(userId).orElseThrow(
        () -> new BaseException(NOT_FOUND_USER_ID));
    return new UserResponseDTO(user);
  }

  // 회원 정보 업데이트
  @Transactional
  public void updateUser(Long userId, UserUpdateRequestDTO requestDTO) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new BaseException(NOT_FOUND_USER_ID));
    if (!isValidPassword(requestDTO.getPassword(), user.getPassword())) {
      throw new BaseException(INVALID_PASSWORD);
    }
    user.update(requestDTO);
  }

  // 비밀번호 업데이트
  @Transactional
  public void updatePassword(Long userId, PwdUpdateRequestDTO requestDTO) {
    User user = userRepository.findById(userId).orElseThrow();

    // 비밀번호 검증
    if (!isValidPassword(requestDTO.getOldPassword(), user.getPassword())) {
      throw new BaseException(INVALID_PASSWORD);
    }
    user.updatePwd(passwordEncoder.encode(requestDTO.getNewPassword()));
  }

  // 비밀번호 일치 검사
  private boolean isValidPassword(String rawPassword, String encodePassword) {
    System.out.println("RAW: " + rawPassword);
    System.out.println("ENCODED: " + passwordEncoder.encode(rawPassword));
    System.out.println("SAVED ENCODED: " + encodePassword);
    return passwordEncoder.matches(rawPassword, encodePassword);
  }

  // 중복 이메일 검사
  private boolean isExistsEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  // 유저 탈퇴
  @Transactional
  public void deactivateUser(Long userId, UserDeactivateRequestDTO requestDTO) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new BaseException(NOT_FOUND_USER_ID));

    // 비밀번호 검증
    if (!isValidPassword(requestDTO.getPassword(), user.getPassword())) {
      throw new BaseException(INVALID_PASSWORD);
    }

    // 탈퇴된 유저인지 확인
    if (user.getDeletedAt() != null) {
      throw new BaseException(DEACTIVATED_USER);
    }
    user.setDeletedAt(LocalDateTime.now());
  }

  // 유저 프로필 이미지 업로드
  @Transactional(rollbackFor = RuntimeException.class)
  public void uploadProfileImg(Long userId, MultipartFile file) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new BaseException(NOT_FOUND_USER_ID));

    try {
      user.setProfileImg(imageService.uploadImage(file));   // 업로드 후 유저 프로필 이미지에 값 설정
    } catch (BaseException e) {
      throw new BaseException(FAILED_UPLOAD_IMAGE);
    }
  }

  // 유저 프로필 이미지 조회
  public Long getProfileImgId(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new BaseException(NOT_FOUND_USER_ID));
    if (user.getProfileImg() != null) {
      return user.getProfileImg().getId();
    } else {
      return null;
    }
  }
}
