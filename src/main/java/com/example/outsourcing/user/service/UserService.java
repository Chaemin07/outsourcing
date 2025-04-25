package com.example.outsourcing.user.service;

import com.example.outsourcing.common.config.PasswordEncoder;
import com.example.outsourcing.user.dto.PwdUpdateRequestDTO;
import com.example.outsourcing.user.dto.UserDeactiveRequestDTO;
import com.example.outsourcing.user.dto.UserResponseDTO;
import com.example.outsourcing.user.dto.UserSignupRequestDTO;
import com.example.outsourcing.user.dto.UserUpdateRequestDTO;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  // 회원 가입
  @Transactional
  public void signup(UserSignupRequestDTO requestDTO) {
    // 유저 이메일(아이디) 중복 검사
    if (!isExistsEmail(requestDTO.getEmail())) {
      // 예외 던지기
    }

    // 비밀번호 인코딩
    String encodedPwd = passwordEncoder.encode(requestDTO.getPassword());
    requestDTO.setPassword(encodedPwd);

    // 저장
    userRepository.save(new User(requestDTO));  // TODO: Mapper or MapStruct 로 DTO-엔티티 변환 구현
  }

  // 회원 조회
  public UserResponseDTO getUser(Long userId) {
    // 유효한 id 인지 검사

    // TODO: 레포지토리 조회 후 DTO 변환해서 반환
    User user = userRepository.findById(userId).orElseThrow(
        () -> new RuntimeException("유저를 찾을 수 없습니다."));
    return new UserResponseDTO(user);
  }

  // 회원 정보 업데이트
  @Transactional
  public void updateUser(Long userId, UserUpdateRequestDTO requestDTO) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new RuntimeException("유저를 찾을 수 없습니다."));
    if (!isValidPassword(requestDTO.getPassword(), user.getPassword())) {
      return;
    }
    user.update(requestDTO);
  }

  // 비밀번호 업데이트
  @Transactional
  public void updatePassword(Long userId, PwdUpdateRequestDTO requestDTO) {
    User user = userRepository.findById(userId).orElseThrow();

    // 비밀번호 검증
    if (!isValidPassword(requestDTO.getOldPwd(), user.getPassword())) {
      return;
    }
    user.updatePwd(passwordEncoder.encode(requestDTO.getNewPwd()));
  }

  // 비밀번호 일치 검사
  private boolean isValidPassword(String rawPassword, String encodePassword) {
    return passwordEncoder.matches(rawPassword, encodePassword);
  }

  // 중복 이메일 검사
  private boolean isExistsEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  // 유저 탈퇴
  @Transactional
  public void deactiveUser(Long userId, UserDeactiveRequestDTO requestDTO) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new RuntimeException("유저를 찾을 수 없습니다."));

    // 비밀번호 검증
    if (!isValidPassword(requestDTO.getPassword(), user.getPassword())) {
      return;
    }

    // 탈퇴된 유저인지 확인
    if (user.getDeletedAt() == null) {
      // TODO: 탈퇴된 회원 예외처리 추가
    }
    user.setDeletedAt(LocalDateTime.now());
  }
}
