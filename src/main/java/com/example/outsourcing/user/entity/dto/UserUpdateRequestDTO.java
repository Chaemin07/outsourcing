package com.example.outsourcing.user.entity.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserUpdateRequestDTO {

  @Size(min = 2, max = 20, message = "닉네임은 2글자 이상 20글자 이하입니다.")
  @Pattern(regexp = "^[a-z|A-Z|0-9|ㄱ-ㅎ|가-힣]+$", message = "닉네임은 영문자, 한글, 숫자만 입력 가능합니다.")
  private String nickname;

  // 단순 이미지 url
  private String profileImg;
}
