package com.example.outsourcing.user.entity;

import com.example.outsourcing.address.entity.Address;
import com.example.outsourcing.cart.entity.Cart;
import com.example.outsourcing.common.entity.BaseEntity;
import com.example.outsourcing.image.entity.Image;
import com.example.outsourcing.user.dto.UserSignupRequestDTO;
import com.example.outsourcing.user.dto.UserUpdateRequestDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(nullable = false)
  String name;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  Role role;

  @Column(nullable = false)
  String nickname;

  @Column(nullable = false, unique = true)
  String email;

  @Column(nullable = false)
  String password;

  @Enumerated(EnumType.STRING)
  OAuth oAuth;

  @Column(nullable = false)
  String phoneNumber;

  @Setter
  @ManyToOne
  @JoinColumn(name = "profile_image_id")
  private Image profileImg;

  @OneToMany(mappedBy = "user")
  private List<Address> addresses;

  @OneToMany(mappedBy = "user")
  private List<Cart> cartList = new ArrayList<>();

  @Setter
  private LocalDateTime deletedAt = null;

  // 회원 정보 수정
  public void update(UserUpdateRequestDTO requestDTO) {
    this.nickname = requestDTO.getNickname();
    // this.profileImg = requestDTO.getProfileImg();   // TODO: Img 레포지토리 사용
  }

  // 비밀번호 수정
  public void updatePwd(String newPassword) {
    this.password = newPassword;
  }

  // TODO: Mapper 혹은 MapStruct 로 통일
  public User(UserSignupRequestDTO requestDTO) {
    this.nickname = requestDTO.getNickname();
    this.password = requestDTO.getPassword();
    this.email = requestDTO.getEmail();
    this.name = requestDTO.getName();
    this.phoneNumber = requestDTO.getPhoneNumber();
    this.role = Role.valueOf(requestDTO.getRole());
  }

  public User(UserSignupRequestDTO requestDTO, Image image) {
    this.nickname = requestDTO.getNickname();
    this.password = requestDTO.getPassword();
    this.email = requestDTO.getEmail();
    this.name = requestDTO.getName();
    this.phoneNumber = requestDTO.getPhoneNumber();
    this.role = Role.valueOf(requestDTO.getRole());
    this.profileImg = image;
  }
}
