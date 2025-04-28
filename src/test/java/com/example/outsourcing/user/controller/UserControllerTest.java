package com.example.outsourcing.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.outsourcing.common.config.PasswordEncoder;
import com.example.outsourcing.user.dto.UserSignupRequestDTO;
import com.example.outsourcing.user.dto.UserUpdateRequestDTO;
import com.example.outsourcing.user.repository.UserRepository;
import com.example.outsourcing.user.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserService userService;

  @Mock
  private UserRepository userRepository;
  @Mock
  private PasswordEncoder passwordEncoder;

  private static UserSignupRequestDTO rq1;
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

    invalidNicknameRq1 = new UserUpdateRequestDTO(
        "☆김수한무★",
        "asdf@1234"
    );
  }

  @Test
  void signup() {
  }

  @Test
  void uploadProfile() {
  }

  @Test
  void getImage() {
  }

  @Test
  void getUser() {
  }

  @Test
  @DisplayName("유저 정보 수정 시 검증 조건을 지키지 않으면 오류 발생")
  void updateUserValid() throws Exception {
    // given : 유저 rq1
    mockMvc.perform(put("/users/1")           // given : 사용자가
            .param("nickname", "☆김수한무★")    // when : 닉네임 검증 조건 위반 시
            .param("password", "asdf@1234"))
        .andExpect(status().isBadRequest())               // then : 예외 발생
        .andExpect(content().string(org.hamcrest.Matchers.containsString("닉네임")));
  }

  @Test
  void updatePassword() {
  }

  @Test
  void deactiveUser() {
  }

  @Test
  void createUserAddress() {
  }

  @Test
  void updateUserAddress() {
  }

  @Test
  void setUserDefaultAddress() {
  }

  @Test
  void deleteUserAddress() {
  }
}