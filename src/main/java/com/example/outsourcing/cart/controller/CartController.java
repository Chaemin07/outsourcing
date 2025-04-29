package com.example.outsourcing.cart.controller;


import com.example.outsourcing.cart.dto.CartRequestDto;
import com.example.outsourcing.cart.dto.CartResponseDto;
import com.example.outsourcing.cart.dto.UpdateCartItemRequestDto;
import com.example.outsourcing.cart.entity.Cart;
import com.example.outsourcing.cart.service.CartService;
import com.example.outsourcing.common.annotation.AuthUser;
import com.example.outsourcing.common.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/cart")
@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addItemtoCart(
            @AuthUser Long userId,
            @RequestBody @Valid CartRequestDto requestDto) {
        String message = "\"장바구니에 메뉴가 추가되었습니다.\"";
        cartService.addItemtoCart(userId, requestDto);
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponseDto>> getCartItems(@AuthUser Long userId) {
        String message = "\"장바구니를 조회했습니다.\"";
        CartResponseDto responseDto = cartService.getCartDetails(userId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, message, responseDto));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<Void>> updateCart(
            @AuthUser Long userId,
            @RequestBody @Valid UpdateCartItemRequestDto requestDto) {
        String message = "\"수정한 메뉴가 장바구니에 반영되었습니다.\"";
        cartService.updateCart(userId, requestDto);
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse<Void>> deleteCartItem(
            @AuthUser Long userId,
            @PathVariable @NotNull Long cartId) {
        cartService.deleteCartItem(userId,cartId);
        String message = "\"해당 메뉴가 장바구니에서 삭제되었습니다.\"";
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    @DeleteMapping("/all")
    public ResponseEntity<ApiResponse<Void>> clearCart(@AuthUser Long userId) {
        cartService.clearCart(userId);
        String message = "\"장바구니가 삭제되었습니다.\"";
        return ResponseEntity.ok(ApiResponse.success(message));
    }
}
