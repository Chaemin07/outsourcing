package com.example.outsourcing.cart.service;

import com.example.outsourcing.cart.dto.*;
import com.example.outsourcing.cart.entity.Cart;
import com.example.outsourcing.cart.entity.SelectedMenuOption;
import com.example.outsourcing.cart.repository.CartRepository;
import com.example.outsourcing.cart.repository.SelectedMenuOptionRepository;
import com.example.outsourcing.menu.entity.Menu;
import com.example.outsourcing.menu.entity.MenuOption;
import com.example.outsourcing.menu.repository.MenuRepository;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.entity.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CartService {
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final CartRepository cartRepository;
    private final SelectedMenuOptionRepository selectedMenuOptionRepository;
    
    @Transactional
    public void addItemtoCart(Long userId,CartRequestDto requestDto) {
        Long menuId = requestDto.getMenuId();
        // TODO 메뉴 아이디를 통해 가게 정보를 가져와야함
        // 장바구니에는 같은 가게의 메뉴만!
        Long findStoreId = menuId + 1L;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다!!"));
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴를 찾을 수 없습니다!!"));
        Optional<Cart> cartOptional = cartRepository.findByUserIdAndMenuId(userId,menuId);
        List<Long> optionIdList = requestDto.getOptionIds();
        // 유저아이디로 저장된 장바구니가 있는경우 → 기존 장바구니 가게와 requestDto가게 정보 비교 필요
        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            if (cart.getStoreId() != requestDto.getStoreId()) {
                // TODO 가게 다르면 사용자에게 메세지 보내야함
                // 기존 장바구니에 있는 가게 메뉴를 담을 건지
                // 아니면 새롭게 들어온 가게 메뉴로 장바구니 담을건지
                // 어떻게 처리하지?
            }
        }
        // 기존 장바구니 있지만, 가게가 같은경우
        // 해당 사용자의 기존 장바구니 없어, 장바구니 새로 만드는경우
        Cart newCart = Cart.builder()
                .quantity(requestDto.getQuantity())
                .storeId(requestDto.getStoreId())
                .user(user)
                .menu(menu)
                .build();
        cartRepository.save(newCart);
        // 메뉴 옵션이 있다면
        if (optionIdList != null) {
            // 선택한 메뉴 옵션 모두 저장
            for (Long optionId : optionIdList) {
                SelectedMenuOption selectedMenuOption = SelectedMenuOption.builder()
                        .menuId(requestDto.getMenuId())
                        .menuOptionId(optionId)
                        .cart(newCart)
                        .build();
                selectedMenuOptionRepository.save(selectedMenuOption);
            }
        }

    }

    @Transactional
    public void updateCart(Long userId, UpdateCartItemRequestDto requestDto) {
        Cart cart = cartRepository.findByUserIdAndMenuId(userId, requestDto.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디의 장바구니가 없습니다!"));
        // 더티 체킹
        cart.updateQuantity(requestDto.getQuantity());
        // 수정할 메뉴 옵션 id가 있다면 → 부분삭제 후 새로 추가한 메뉴 옵션 추가
        // 부분 삭제
        if (requestDto.getOptionIdsToRemove() != null) {
            for (Long removeOptionId : requestDto.getOptionIdsToRemove()) {
                selectedMenuOptionRepository.deleteById(removeOptionId);
            }
        }
        // 부분 추가
        if (requestDto.getOptionIdsToAdd() != null) {
            for (Long addOptionId : requestDto.getOptionIdsToAdd()) {
                SelectedMenuOption newOption = SelectedMenuOption.builder()
                        .cart(cart)
                        .menuId(requestDto.getMenuId())
                        .menuOptionId(addOptionId)
                        .build();
                selectedMenuOptionRepository.save(newOption);
            }
        }

    }

    // TODO 엔티티를 DTO로 변환필요
    public CartResponseDto getCartDetails(Long userId) {
        List<CartItemDto> cartItemDtoList = new ArrayList<>();
        List<Cart> cartList = cartRepository.findByUserId(userId);
        Integer totalPrice = 0;
        for (Cart cart : cartList) {
            Integer optionTotalPrice = 0;
            Long menuId = cart.getMenu().getId();
            String menuName = cart.getMenu().getName();
            Integer quantity = cart.getQuantity();
            Integer price = cart.getMenu().getPrice();

            // 메뉴 id를 통해 해당 메뉴에 추가된 메뉴옵션(엔티티) 리스트 가져오기
            List<MenuOption> menuOptionList = selectedMenuOptionRepository.findByMenuId(menuId);
            // 메뉴옵션(엔티티) optionDtoList(dto)로 변환
            List<OptionDto> optionDtoList = menuOptionList.stream()
                    .map(menuOption -> new OptionDto(menuOption.getId(), menuOption.getOptionName(), menuOption.getPrice()))
                    .collect(Collectors.toList());
            // 메뉴별 옵션값 합계 추가
            for (OptionDto optionDto : optionDtoList) {
                optionTotalPrice += optionDto.getOptionPrice();
            }
            // 중간 메뉴당 가격 합계(메뉴가격*수량+옵션들 가격)
            Integer subTotalPrice = price * quantity + optionTotalPrice;
            // 총 가격 합계
            totalPrice += subTotalPrice;
            // CartItemDto 생성
            CartItemDto cartItemDto = CartItemDto.builder()
                    .cartId(cart.getId())
                    .menuId(menuId)
                    .menuName(menuName)
                    .quantity(quantity)
                    .menuPrice(price)
                    .subTotalPrice(subTotalPrice)
                    .options(optionDtoList)
                    .build();

            cartItemDtoList.add(cartItemDto);
        }
        return new CartResponseDto(cartItemDtoList, totalPrice);

    }

    public void deleteCartItem(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    public void clearCart(Long userId) {
        cartRepository.deleteByUserId(userId);
    }
}
