package com.example.outsourcing.user.dto;

import com.example.outsourcing.user.entity.Role;
import java.util.List;
import lombok.Getter;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;

@Getter
public class UserResponseDTO {

  private String name;
  private String Email;
  private String phoneNumber;
  private List<Address> addresses;
  private String nickname;
  private Role role;
  private String profileImg;
}
