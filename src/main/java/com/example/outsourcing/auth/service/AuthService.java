package com.example.outsourcing.auth.service;

import static com.example.outsourcing.common.exception.ErrorCode.DEACTIVATED_USER;
import static com.example.outsourcing.common.exception.ErrorCode.INVALID_PASSWORD;
import static com.example.outsourcing.common.exception.ErrorCode.NOT_FOUND_USER_ID;

import com.example.outsourcing.auth.dto.LoginResponseDTO;
import com.example.outsourcing.common.config.PasswordEncoder;
import com.example.outsourcing.common.exception.BaseException;
import com.example.outsourcing.jwt.JwtUtil;
import com.example.outsourcing.user.dto.UserLoginRequestDTO;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  // 로그인
  public LoginResponseDTO login(UserLoginRequestDTO requestDTO) {
    // 이메일 검증
    User user = userRepository.findUserByEmail(requestDTO.getEmail()).orElseThrow(
        () -> new BaseException(NOT_FOUND_USER_ID));

    // 탈퇴한 사용자 검증
    if (user.getDeletedAt() != null) {
      throw new BaseException(DEACTIVATED_USER);
    }

    // 비밀번호 검증
    if (!isValidPassword(requestDTO.getPassword(), user.getPassword())) {
      throw new BaseException(INVALID_PASSWORD);
    }

    String token = jwtUtil.createToken(user.getId(), user.getEmail(), user.getRole());

    return new LoginResponseDTO(token);
  }

  // 비밀번호 일치 검사
  private boolean isValidPassword(String rawPassword, String encodePassword) {
    return passwordEncoder.matches(rawPassword, encodePassword);
  }
}
