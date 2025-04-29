package com.example.outsourcing.user.service;

import static com.example.outsourcing.common.exception.ErrorCode.CONFLICT_EMAIL;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.example.outsourcing.address.service.AddressService;
import com.example.outsourcing.auth.service.AuthService;
import com.example.outsourcing.common.config.PasswordEncoder;
import com.example.outsourcing.common.exception.BaseException;
import com.example.outsourcing.user.dto.UserSignupRequestDTO;
import com.example.outsourcing.user.dto.UserUpdateRequestDTO;
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

  @Mock
  private AuthService authService;

  @InjectMocks
  private UserService userService;

  private static UserSignupRequestDTO rq1;
  private static UserSignupRequestDTO duplicatedRq1;
  private static UserUpdateRequestDTO invalidNicknameRq1;

  @BeforeAll
  static void setupData() {
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

    invalidNicknameRq1 = new UserUpdateRequestDTO(
        "☆김수한무★",
        "asdf@1234"
    );
  }

  @Test
  @DisplayName("중복 이메일로 회원 가입")
  void signupWithDuplicatedEmail() {
    // given : 중복 이메일 요청

    // 유저 1 회원가입
    userService.signup(rq1);

    // when : 유저 1과 중복 이메일로 회원가입
    when(userRepository.existsByEmail("kimsh@gmail.com")).thenReturn(true);

    // then : 예외 발생
    assertThatThrownBy(() -> userService.signup(duplicatedRq1))
        .isInstanceOf(BaseException.class)
        .hasMessageContaining(CONFLICT_EMAIL.getMessage());
  }

  @Test
  void updatePassword() {
  }

//  @Test
//  @DisplayName("탈퇴한 사용자 아이디로 로그인 시 오류 발생")
//  void signinDeactivatedUser() {
//    // given : 유저 rq1
//    User user = new User(rq1);
//    user.setPassword(passwordEncoder.encode(rq1.getPassword()));
//
//    // when : 탈퇴 후 로그인 시
//    user.setDeletedAt(LocalDateTime.now());
//
//    when(userRepository.findUserByEmail(rq1.getEmail())).thenReturn(Optional.of(user));
//    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
//
//    // then : 예외 발생
//    assertThatThrownBy(() -> authService.login(
//        new UserLoginRequestDTO(rq1.getEmail(), rq1.getPassword())))
//        .isInstanceOf(BaseException.class)
//        .hasMessageContaining(DEACTIVATED_USER.getMessage());
//  }

  @Test
  @DisplayName("프로필 이미지 조회 성공")
  void uploadProfileImg() {
  }

  @Test
  void getProfileImgId() {
  }
}
