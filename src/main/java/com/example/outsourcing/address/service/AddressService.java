package com.example.outsourcing.address.service;

import static com.example.outsourcing.common.exception.ErrorCode.FORBIDDEN_ADDRESS;
import static com.example.outsourcing.common.exception.ErrorCode.NOT_FOUND_USER_ID;
import static com.example.outsourcing.common.exception.ErrorCode.OVER_MAX_ADDRESSES;

import com.example.outsourcing.address.dto.UpdateUserAddressRequestDTO;
import com.example.outsourcing.address.entity.Address;
import com.example.outsourcing.common.exception.BaseException;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressService {

  private final UserRepository userRepository;

  // 유저 주소 등록
  @Transactional
  public void createAddress(Long userId, UpdateUserAddressRequestDTO requestDTO) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new BaseException(NOT_FOUND_USER_ID));

    // 최대 주소(3개) 인지 검사
    if (user.hasMaxAddresses()) {
      throw new BaseException(OVER_MAX_ADDRESSES);
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
        () -> new BaseException(NOT_FOUND_USER_ID));

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

    throw new BaseException(FORBIDDEN_ADDRESS);
  }

  // 유저 주소 디폴트 설정
  @Transactional
  public void setDefaultAddress(Long userId, Long addressId) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new BaseException(NOT_FOUND_USER_ID));

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

    throw new BaseException(FORBIDDEN_ADDRESS);
  }

  @Transactional
  public void deleteAddress(Long userId, Long addressId) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new BaseException(NOT_FOUND_USER_ID));
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
    throw new BaseException(FORBIDDEN_ADDRESS);
  }
}
