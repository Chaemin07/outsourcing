package com.example.outsourcing.user.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.example.outsourcing.address.service.AddressService;
import com.example.outsourcing.common.config.PasswordEncoder;
import com.example.outsourcing.user.dto.UserSignupRequestDTO;
import com.example.outsourcing.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private AddressService addressService;

  @InjectMocks
  private UserService userService;

  private static UserSignupRequestDTO rq1;
  private static UserSignupRequestDTO duplicatedRq1;

  @BeforeAll
  static void setData() {
    rq1 = new UserSignupRequestDTO(
        "김수한무",
        "USER",
        "김수한",
        "kimsh@gmail.com",
        "asdf@1234",
        "010-7777-8888",
        "서울시 성동구 성동로 11"
    );

    duplicatedRq1 = new UserSignupRequestDTO(
        "김수수",
        "USER",
        "김소한",
        "kimsh@gmail.com",
        "asdf@1234",
        "010-7777-8888",
        "서울시 성동구 성동로 11"
    );
  }

  @Test
  @DisplayName("중복 이메일로 회원 가입")
  void signup() {
    // given : 중복 이메일 요청

    // 유저 1 회원가입
    userService.signup(rq1);

    // when : 유저 1과 중복 이메일로 회원가입
    when(userRepository.existsByEmail("kimsh@gmail.com")).thenReturn(true);

    // then : 예외 발생
    assertThatThrownBy(() -> userService.signup(duplicatedRq1))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("이메일 중복입니다.");
  }

  @Test
  @DisplayName("잘못된 id로 회원 조회")
  void getUser() {
    // given : 중복 이메일 요청

    // 유저 1 회원가입
    userService.signup(rq1);

    // when : 유저 1과 중복 이메일로 회원가입
    when(userRepository.existsByEmail("kimsh@gmail.com")).thenReturn(true);

    // then : 예외 발생
    assertThatThrownBy(() -> userService.signup(duplicatedRq1))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("이메일 중복입니다.");
  }

  @Test
  void updateUser() {
  }

  @Test
  void updatePassword() {
  }

  @Test
  void deactivateUser() {
  }

  @Test
  void uploadProfileImg() {
  }

  @Test
  void getProfileImgId() {
  }
}