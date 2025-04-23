package com.example.outsourcing.user.entity.service;

import com.example.outsourcing.user.entity.dto.UserSignupRequestDTO;
import com.example.outsourcing.user.entity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  // 회원 가입
  @Transactional
  public void signup(UserSignupRequestDTO requestDTO) {
    // 유저 이메일(아이디) 중복 검사
    if(!isExistsEmail(requestDTO.getEmail())) {
      // 예외 던지기
    }

    // 저장
    userRepository.save(requestDTO);  // TODO: Mapper or MapStruct 로 DTO-엔티티 변환 구현
  }

  // 중복 이메일 검사
  private boolean isExistsEmail(String email) {
    return userRepository.existsByEmail(email);
  }
}
