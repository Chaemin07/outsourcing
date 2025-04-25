package com.example.outsourcing.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserUpdateRequestDTO {

  @Size(min = 2, max = 20, message = "닉네임은 2글자 이상 20글자 이하입니다.")
  @Pattern(regexp = "^[a-z|A-Z|0-9|ㄱ-ㅎ|가-힣]+$", message = "닉네임은 영문자, 한글, 숫자만 입력 가능합니다.")
  private String nickname;

  @NotBlank(message = "기존 비밀번호는 필수값입니다.")
  @Size(min = 8, max = 20, message = "기존 비밀번호는 8자 이상 20자리 이하입니다.")
  @Pattern(regexp = ".*\\d.*", message = "기존 비밀번호는 숫자를 포함해야 합니다.")
  @Pattern(regexp = ".*[a-zA-Z].*", message = "기존 비밀번호는 영문자를 포함해야 합니다.")
  @Pattern(regexp = ".*[@_\\-#$].*", message = "기존 비밀번호는 특수문자를 포함해야 합니다.")
  private String password;
}
