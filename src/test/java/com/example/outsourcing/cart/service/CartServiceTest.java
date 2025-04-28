package com.example.outsourcing.cart.service;

import com.example.outsourcing.cart.dto.CartRequestDto;
import com.example.outsourcing.cart.entity.Cart;
import com.example.outsourcing.cart.entity.SelectedMenuOption;
import com.example.outsourcing.cart.repository.CartRepository;
import com.example.outsourcing.cart.repository.SelectedMenuOptionRepository;
import com.example.outsourcing.menu.entity.Menu;
import com.example.outsourcing.menu.repository.MenuRepository;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("CartService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private SelectedMenuOptionRepository selectedMenuOptionRepository;

    @InjectMocks
    private CartService cartService;

    @DisplayName("기존에 없는 사용자일 경우 예외 발생")
    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // given
        Long userId = 111L;
        Long menuId = 222L;
        Long storeId = 333L;
        // 장바구니 요청 DTO 생성:  메뉴 옵션은 없는 상태로 설정
        CartRequestDto cartRequestDto = new CartRequestDto(storeId, menuId, 1, List.of());

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        // 사용자 조회 결과가 없으면 RuntimeException 발생해야 함
        assertThrows(RuntimeException.class, () -> cartService.addItemtoCart(userId, cartRequestDto));

        verify(userRepository, times(1)).findById(userId);
        // menuRepository, cartRepository 다른 불필요한 레포지토리 호출 방지
        verifyNoMoreInteractions(menuRepository, cartRepository);
    }

    @DisplayName("없는 메뉴일 경우 예외 발생")
    @Test
    void shouldThrowExceptionWhenMenuNotFound() {
        // given
        Long userId = 111L;
        Long menuId = 222L;
        Long storeId = 333L;
        CartRequestDto cartRequestDto = new CartRequestDto(storeId, menuId, 1, List.of());

        User mockUser = User.builder().id(userId).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(menuRepository.findById(menuId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> cartService.addItemtoCart(userId, cartRequestDto));

        verify(userRepository, times(1)).findById(userId);
        verify(menuRepository, times(1)).findById(menuId);
        verifyNoMoreInteractions(cartRepository);
    }




    @DisplayName("기존 장바구니가 없는 경우 새로운 장바구니가 추가되어야 한다")
    @Test
    void shouldAddNewCartWhenNoExistingCart() {

        // given
        Long userId = 111L;
        Long menuId = 222L;
        Long storeId = 333L;
        CartRequestDto cartRequestDto = new CartRequestDto(storeId, menuId, 10, List.of(10L, 20L));

        User mockUser = User.builder().id(userId).build();
        Menu mockMenu = Menu.builder().id(menuId).name("Hamburger").price(8000).build();

        //Stubbing
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(mockMenu));
        when(cartRepository.findByUserIdAndMenuId(userId, menuId)).thenReturn(Optional.empty());

        // when
        cartService.addItemtoCart(userId, cartRequestDto);

        // then
        verify(userRepository, times(1)).findById(userId);
        verify(menuRepository, times(1)).findById(menuId);
        verify(cartRepository, times(1)).findByUserIdAndMenuId(userId, menuId);
        verify(cartRepository, times(1)).save(any(Cart.class));
        verify(selectedMenuOptionRepository, times(2)).save(any(SelectedMenuOption.class));
    }

    @Test
    void updateCart() {
    }

    @Test
    void getCartDetails() {
    }

    @Test
    void deleteCartItem() {
    }

    @Test
    void clearCart() {
    }
}