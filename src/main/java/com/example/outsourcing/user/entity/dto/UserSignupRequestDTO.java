package com.example.outsourcing.user.entity.dto;

import com.example.outsourcing.address.entity.address;
import com.example.outsourcing.user.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;

@Getter
public class UserSignupRequestDTO {
  @NotBlank(message = "이름은 필수값입니다.")
  private String name;

  @NotBlank(message = "이메일은 필수값입니다.")
  @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "유효한 이메일 형식이 아닙니다.")
  private String email;

  @NotBlank(message = "비밀번호는 필수값입니다.")
  @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자리 이하입니다.")
  @Pattern(regexp = ".*\\d.*", message = "새 비밀번호는 숫자를 포함해야 합니다.")
  @Pattern(regexp = ".*[a-zA-Z].*", message = "새 비밀번호는 영문자를 포함해야 합니다.")
  @Pattern(regexp = ".*[@_\\-#$].*", message = "비밀번호는 특수문자를 포함해야 합니다.")
  private String password;

  @NotBlank(message = "전화번호는 필수값입니다.")
  @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$", message = "유효한 휴대폰 번호 형식이 아닙니다.") // 010-xxxx-xxxx 만 되도록 허용
  private String phoneNumber;

  private Address address;

  @NotBlank(message = "닉네임은 필수값입니다.")
  @Size(min = 2, max = 20, message = "닉네임은 2글자 이상 20글자 이하입니다.")
  @Pattern(regexp = "^[a-z|A-Z|0-9|ㄱ-ㅎ|가-힣]+$", message = "닉네임은 영문자, 한글, 숫자만 입력 가능합니다.")
  String nickname;

  // TODO: 추후 Enum Validator 사용
  @NotBlank(message = "권한은 필수값입니다.")
  Role role;

  // profile img; 연관관계 맵핑
}
