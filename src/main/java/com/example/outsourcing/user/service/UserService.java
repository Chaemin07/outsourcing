package com.example.outsourcing.user.service;

import com.example.outsourcing.address.dto.UpdateUserAddressRequestDTO;
import com.example.outsourcing.address.entity.Address;
import com.example.outsourcing.common.config.PasswordEncoder;
import com.example.outsourcing.image.service.ImageService;
import com.example.outsourcing.user.dto.PwdUpdateRequestDTO;
import com.example.outsourcing.user.dto.UserDeactiveRequestDTO;
import com.example.outsourcing.user.dto.UserResponseDTO;
import com.example.outsourcing.user.dto.UserSignupRequestDTO;
import com.example.outsourcing.user.dto.UserUpdateRequestDTO;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final ImageService imageService;
  private final PasswordEncoder passwordEncoder;

  // 회원 가입
  @Transactional
  public void signup(UserSignupRequestDTO requestDTO) {
    // 유저 이메일(아이디) 중복 검사
    if (isExistsEmail(requestDTO.getEmail())) {
      throw new RuntimeException("이메일 중복입니다.");
    }

    // 비밀번호 인코딩
    String encodedPwd = passwordEncoder.encode(requestDTO.getPassword());
    requestDTO.setPassword(encodedPwd);

    // 저장
    userRepository.save(new User(requestDTO));  // TODO: Mapper or MapStruct 로 DTO-엔티티 변환 구현
  }

  // 회원 조회
  public UserResponseDTO getUser(Long userId) {
    // 유효한 id 인지 검사
    User user = userRepository.findById(userId).orElseThrow(
        () -> new RuntimeException("유저를 찾을 수 없습니다."));
    return new UserResponseDTO(user);
  }

  // 회원 정보 업데이트
  @Transactional
  public void updateUser(Long userId, UserUpdateRequestDTO requestDTO) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new RuntimeException("유저를 찾을 수 없습니다."));
    if (!isValidPassword(requestDTO.getPassword(), user.getPassword())) {
      throw new RuntimeException("비밀번호가 일치하지 않습니다.");
    }
    user.update(requestDTO);
  }

  // 비밀번호 업데이트
  @Transactional
  public void updatePassword(Long userId, PwdUpdateRequestDTO requestDTO) {
    User user = userRepository.findById(userId).orElseThrow();

    // 비밀번호 검증
    if (!isValidPassword(requestDTO.getOldPassword(), user.getPassword())) {
      throw new RuntimeException("비밀번호가 일치하지 않습니다.");
    }
    user.updatePwd(passwordEncoder.encode(requestDTO.getNewPassword()));
  }

  // 비밀번호 일치 검사
  private boolean isValidPassword(String rawPassword, String encodePassword) {
    return passwordEncoder.matches(rawPassword, encodePassword);
  }

  // 중복 이메일 검사
  private boolean isExistsEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  // 유저 탈퇴
  @Transactional
  public void deactiveUser(Long userId, UserDeactiveRequestDTO requestDTO) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new RuntimeException("유저를 찾을 수 없습니다."));

    // 비밀번호 검증
    if (!isValidPassword(requestDTO.getPassword(), user.getPassword())) {
      throw new RuntimeException("비밀번호가 일치하지 않습니다.");
    }

    // 탈퇴된 유저인지 확인
    if (user.getDeletedAt() != null) {
      throw new RuntimeException("이미 탈퇴한 회원입니다.");
    }
    user.setDeletedAt(LocalDateTime.now());
  }

  // 유저 프로필 이미지 업로드
  @Transactional(rollbackFor = RuntimeException.class)
  public void uploadProfileImg(Long userId, MultipartFile file) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new RuntimeException("유저를 찾을 수 없습니다."));

    try {
      user.setProfileImg(imageService.uploadImage(file));   // 업로드 후 유저 프로필 이미지에 값 설정
    } catch (RuntimeException e) {
      new RuntimeException("파일 업로드에 실패하였습니다.", e);
    }
  }

  // 유저 프로필 이미지 조회
  public Long getProfileImgId(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new RuntimeException("유저를 찾을 수 없습니다."));
    if (user.getProfileImg() != null) {
      return user.getProfileImg().getId();
    } else {
      return null;
    }
  }

  // 유저 주소 등록
  @Transactional
  public void createAddress(Long userId, UpdateUserAddressRequestDTO requestDTO) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new RuntimeException("유저를 찾을 수 없습니다."));

    // 최대 주소(3개) 인지 검사
    if (user.hasMaxAddresses()) {
      throw new RuntimeException("주소는 최대 3개 등록 가능합니다.");
    }

    Address address = new Address(requestDTO, user);

    // 주소를 가지지 않거나, 새로 추가하면서 디폴트 설정을 해주고 싶다면
    if (user.getAddresses().isEmpty() || requestDTO.isDefault()) {
      address.setDefault(true);
      user.getAddresses().add(address);
      return;
    }

    // 기존 주소가 있고, 디폴트 주소를 설정하고 싶다면
    if (requestDTO.isDefault()) {
      for (Address nowAddress : user.getAddresses()) {
        // 디폴트 주소 획득
        if (nowAddress.isDefault()) {
          nowAddress.setDefault(false);
        }
      }
    }

    user.getAddresses().add(address);
  }

  // 유저 주소 업데이트
  @Transactional
  public void updateAddress(Long userId, Long addressId,
      UpdateUserAddressRequestDTO requestDTO) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new RuntimeException("유저를 찾을 수 없습니다."));

    Address defaultAddress = null;
    Address targetAddress = null;

    for (Address address : user.getAddresses()) {
      // 디폴트 주소 획득
      if (defaultAddress == null && address.isDefault()) {
        defaultAddress = address;
      }
      // 디폴트 설정 대상 주소
      if (targetAddress == null && Objects.equals(address.getId(), addressId)) {
        targetAddress = address;
      }
    }

    // 디폴트 설정 대상 주소 조회 성공이라면
    if (targetAddress != null) {
      targetAddress.setAddress(requestDTO.getAddress());

      // 디폴트 주소 설정 한다면
      if (requestDTO.isDefault()) {
        targetAddress.setDefault(true);

        // 디폴트 주소가 있다면
        if (defaultAddress != null) {
          defaultAddress.setDefault(false);
        }
      }
      return;
    }

    throw new RuntimeException("사용자의 주소가 아닙니다.");
  }

  // 유저 주소 디폴트 설정
  @Transactional
  public void setDefaultAddress(Long userId, Long addressId) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new RuntimeException("유저를 찾을 수 없습니다."));

    Address defaultAddress = null;
    Address targetAddress = null;

    for (Address address : user.getAddresses()) {
      // 디폴트 주소 획득
      if (defaultAddress == null && address.isDefault()) {
        defaultAddress = address;
      }
      // 디폴트 설정 대상 주소
      if (targetAddress == null && Objects.equals(address.getId(), addressId)) {
        targetAddress = address;
      }
    }

    // 디폴트 설정 대상 주소 조회 성공이라면
    if (targetAddress != null) {
      targetAddress.setDefault(true);
      // 디폴트 주소가 있다면
      if (defaultAddress != null) {
        defaultAddress.setDefault(false);
      }
      return;
    }

    throw new RuntimeException("사용자의 주소가 아닙니다.");
  }

  @Transactional
  public void deleteAddress(Long userId, Long addressId) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new RuntimeException("유저를 찾을 수 없습니다."));
    List<Address> addresses = user.getAddresses();
    Address defaultAddress = null;
    Address targetAddress = null;

    for (Address address : user.getAddresses()) {
      // 디폴트 주소 획득
      if (defaultAddress == null && address.isDefault()) {
        defaultAddress = address;
      }
      // 삭제 대상 주소
      if (targetAddress == null && Objects.equals(address.getId(), addressId)) {
        targetAddress = address;
      }
      // 디폴트, 삭제 주소 모두 획득
      if (targetAddress != null && defaultAddress != null) {
        if (Objects.equals(defaultAddress.getId(), targetAddress.getId())) {
          addresses.remove(address);
          addresses.get(addresses.size() - 1).setDefault(true);
          return;
        }
        addresses.remove(address);
        return;
      }
    }
    throw new RuntimeException("사용자의 주소가 아닙니다.");
  }
}
