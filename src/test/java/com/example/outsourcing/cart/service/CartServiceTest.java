package com.example.outsourcing.cart.service;

import com.example.outsourcing.cart.dto.CartRequestDto;
import com.example.outsourcing.cart.dto.CartResponseDto;
import com.example.outsourcing.cart.dto.UpdateCartItemRequestDto;
import com.example.outsourcing.cart.entity.Cart;
import com.example.outsourcing.cart.entity.SelectedMenuOption;
import com.example.outsourcing.cart.repository.CartRepository;
import com.example.outsourcing.cart.repository.SelectedMenuOptionRepository;
import com.example.outsourcing.common.exception.BaseException;
import com.example.outsourcing.common.exception.ErrorCode;
import com.example.outsourcing.menu.entity.Menu;
import com.example.outsourcing.menu.entity.MenuOption;
import com.example.outsourcing.menu.repository.MenuRepository;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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
        // 메뉴 조회시 값이 null인 Optional리턴
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
    @DisplayName("장바구니 옵션 추가 테스트")
    @Test
    void updateCartWithOptionAddition() {
        // given
        Long userId = 111L;
        Long menuId = 222L;
        // 장바구니 메뉴 옵션 30,40 추가
        UpdateCartItemRequestDto requestDto = new UpdateCartItemRequestDto(menuId, 2, List.of(30L, 40L), null);
        Cart cart = mock(Cart.class);
        when(cartRepository.findByUserIdAndMenuId(userId, menuId)).thenReturn(Optional.of(cart));
        // when
        cartService.updateCart(userId, requestDto);
        // then
        // 메뉴 수량 테스트
        verify(cart, times(1)).updateQuantity(2);
        // 옵션은 row로 관리되니 2번 실행되어야함
        verify(selectedMenuOptionRepository, times(2)).save(any(SelectedMenuOption.class));
    }

    @DisplayName("장바구니 옵션 제거 테스트")
    @Test
    void updateCartWithOptionRemoval() {
        // given
        Long userId = 111L;
        Long menuId = 222L;
        UpdateCartItemRequestDto requestDto = new UpdateCartItemRequestDto(menuId, 3, null, List.of(10L, 20L));

        Cart cart = mock(Cart.class);
        when(cartRepository.findByUserIdAndMenuId(userId, menuId)).thenReturn(Optional.of(cart));

        // when
        cartService.updateCart(userId, requestDto);

        // then
        // 메뉴 수량 테스트
        verify(cart, times(1)).updateQuantity(3);
        // 각각의 옵션 ID 1번씩 삭제 검증
        verify(selectedMenuOptionRepository, times(1)).deleteById(10L);
        verify(selectedMenuOptionRepository, times(1)).deleteById(20L);
    }


    @DisplayName("장바구니가 만료되었을 때 CART_PERIOD_EXPIRED 예외 발생")
    @Test
    void getCartDetailsThrowsWhenCartExpired() {
        // given
        Long userId = 111L;
        CartService spyCartService = Mockito.spy(cartService); // autoClearExpiredCart mocking 위해 spy 사용
        doReturn(true).when(spyCartService).autoClearExpiredCart(userId);

        // when & then
        BaseException exception = assertThrows(BaseException.class, () -> spyCartService.getCartDetails(userId));
        assertEquals(ErrorCode.CART_PERIOD_EXPIRED, exception.getErrorCode());
    }


    @DisplayName("장바구니 주인이 아닐 경우 FORBIDDEN_CART_ACCESS 예외 발생")
    @Test
    void deleteCartItemThrowsWhenUserIsNotOwner() {
        // given
        Long userId = 111L;
        Long cartId = 222L;
        Cart cart = Cart.builder()
                .id(cartId)
                .user(User.builder().id(123L).build()) // 다른 사용자
                .build();
        //  cart 객체 반환
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        // when & then
        BaseException exception = assertThrows(BaseException.class, () -> cartService.deleteCartItem(userId, cartId));
        assertEquals(ErrorCode.FORBIDDEN_CART_ACCESS, exception.getErrorCode());

        verify(cartRepository, times(1)).findById(cartId);
        // deleteById는 호출되면 안됨
        verify(cartRepository, never()).deleteById(any());
    }



    @DisplayName("장바구니가 비어있을 때 deleteByUserId 호출 확인")
    @Test
    void clearCartWhenCartIsEmpty() {
        // given
        Long userId = 111L;

        // when
        cartService.clearCart(userId);

        // then
        verify(cartRepository, times(1)).deleteByUserId(userId);
        // 추가 호출 여부 체크
        verifyNoMoreInteractions(cartRepository);
    }

    @DisplayName("장바구니가 만료되지 않았을 때 삭제하지 않고 false 반환")
    @Test
    void autoClearExpiredCartShouldNotClearWhenNotExpired() {
        // given
        Long userId = 111L;
        LocalDateTime recentTime = LocalDateTime.now().plusHours(10);
        when(cartRepository.findMaxUpdatedAtByUserId(userId)).thenReturn(Optional.of(recentTime));

        // when
        boolean result = cartService.autoClearExpiredCart(userId);

        // then
        assertFalse(result);
        verify(cartRepository, times(1)).findMaxUpdatedAtByUserId(userId);
        verify(cartRepository, never()).deleteByUserId(any());
    }
}