package com.example.outsourcing.address.service;

import static com.example.outsourcing.common.exception.ErrorCode.OVER_MAX_ADDRESSES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.example.outsourcing.address.dto.UpdateUserAddressRequestDTO;
import com.example.outsourcing.address.entity.Address;
import com.example.outsourcing.common.config.PasswordEncoder;
import com.example.outsourcing.common.exception.BaseException;
import com.example.outsourcing.user.entity.Role;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @InjectMocks
  private AddressService addressService;

  private static User user;
  private static Address address1;
  private static Address address2;
  private static Address address3;
  private static Address address4;
  private static UpdateUserAddressRequestDTO address4rq;

  @BeforeEach
  void setupData() {
    user = new User();
    user = User.builder()
        .id(1L)
        .name("김수한무")
        .role(Role.USER)
        .nickname("김수한무")
        .email("kimsh@gmail.com")
        .password("asdf@1234")
        .build();
    address1 = Address.builder()
        .address("서울시 성동구 성동로 11")
        .isDefault(true)
        .id(1L)
        .build();
    address2 = Address.builder()
        .address("부산시 성동구 성동로 55")
        .isDefault(false)
        .id(2L)
        .build();
    address3 = Address.builder()
        .address("인천시 성동구 성동로 99")
        .isDefault(false)
        .id(3L)
        .build();
    address4 = Address.builder()
        .address("대구시 성동구 성동로 33")
        .isDefault(true)
        .id(4L)
        .build();
    address4rq = new UpdateUserAddressRequestDTO("대구시 성동구 성동로 33", true);
    user.getAddresses().add(address1);
    user.getAddresses().add(address2);
  }

  @Test
  @DisplayName("주소 정상추가")
  void createAddress_success() {
    // given : 최대 주소가 아닐 때 (2개)

    // when & then : 주소 추가하여 성공
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));  // 유저는 찾았다고 치고
    addressService.createAddress(user.getId(), address4rq);

    assertThat(user.getAddresses()).hasSize(3);
  }

  @Test
  @DisplayName("최대 주소 (3개)인 상태에서 주소 추가 시 예외 발생")
  void createAddress_failed() {
    // given : 최대 주소일 때 (3개)
    user.getAddresses().add(address3);

    // when & then : 주소 추가 시
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));  // 유저는 찾았다고 치고
    assertThatThrownBy(() -> addressService.createAddress(user.getId(), address4rq))
        .isInstanceOf(BaseException.class)
        .hasMessageContaining(OVER_MAX_ADDRESSES.getMessage());
  }

  @Test
  @DisplayName("주소지/대표 변경한 경우, 기존의 대표 주소는 false 가 된다")
  void updateAddress() {
    // given : 주소가 3개일 때
    user.getAddresses().add(address3);

    // when && then : 주소지/대표 변경한 경우 기존의 대표 주소는 false 가 되어야한다
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));  // 유저는 찾았다고 치고
    addressService.updateAddress(1L, 3L, address4rq);
    assertThat(address1.isDefault()).isFalse();
  }

  @Test
  @DisplayName("대표 주소를 변경한 경우, 기존의 대표 주소는 false 가 된다")
  void setDefaultAddress() {
    // given

    // when && then : 대표 주소 변경 시 기존의 대표 주소는 false 가 되어야한다
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));  // 유저는 찾았다고 치고
    addressService.setDefaultAddress(1L, 2L);
    assertThat(address1.isDefault()).isFalse();
  }

  @Test
  @DisplayName("대표 주소를 삭제한 경우, 가장 최근의 주소가 대표가 된다")
  void deleteAddress() {
    // given : 주소가 3개 일때
    user.getAddresses().add(address3);

    // when : 1번째 주소를 삭제하면
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));  // 유저는 찾았다고 치고
    addressService.deleteAddress(1L, 1L);

    // then : 3번째 주소가 대표가 된다
    assertThat(address3.isDefault()).isTrue();
  }
}