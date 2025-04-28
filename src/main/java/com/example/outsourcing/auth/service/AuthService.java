package com.example.outsourcing.auth.service;

import com.example.outsourcing.auth.dto.LoginResponseDTO;
import com.example.outsourcing.common.config.PasswordEncoder;
import com.example.outsourcing.common.exception.BaseException;
import com.example.outsourcing.common.exception.ErrorCode;
import com.example.outsourcing.jwt.JwtUtil;
import com.example.outsourcing.user.dto.userLoginRequestDTO;
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
  public LoginResponseDTO login(userLoginRequestDTO requestDTO) {
    // 이메일 검증
    User user = userRepository.findUserByEmail(requestDTO.getEmail()).orElseThrow(
        () -> new BaseException(ErrorCode.INVALID_EMAIL));

    // 비밀번호 검증
    if (!isValidPassword(requestDTO.getPassword(), user.getPassword())) {
      throw new BaseException(ErrorCode.INVALID_PASSWORD);
    }

    String token = jwtUtil.createToken(user.getId(), user.getEmail(), user.getRole());

    return new LoginResponseDTO(token);
  }

  // 비밀번호 일치 검사
  private boolean isValidPassword(String rawPassword, String encodePassword) {
    return passwordEncoder.matches(rawPassword, encodePassword);
  }
}
