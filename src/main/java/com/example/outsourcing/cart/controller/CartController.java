package com.example.outsourcing.cart.controller;


import com.example.outsourcing.cart.dto.CartRequestDto;
import com.example.outsourcing.cart.dto.CartResponseDto;
import com.example.outsourcing.cart.dto.UpdateCartItemRequestDto;
import com.example.outsourcing.cart.entity.Cart;
import com.example.outsourcing.cart.service.CartService;
import com.example.outsourcing.common.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<Void>> addItemtoCart(@RequestBody CartRequestDto requestDto) {
        String message = "\"장바구니에 메뉴가 추가되었습니다.\"";
        Long userId = 1L;
        // TODO 사용자 id도 카트에 넣어줘야함,
        cartService.addItemtoCart(userId, requestDto);
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponseDto>> getCartItems() {
        // TODO JWT 토큰을 통해서 유저아이디 가져오고, 장바구니 있는지 조회 필요
        // TODO JWT토큰 가져오면 이거 삭제해야함, 더미 유저있다고 가정
        Long userId = 1L;
        // TODO 컨트롤러에서 장바구니 엔티티를 가져다가 리턴해준다? NONO 아닌거 같음
        String message = "\"장바구니를 조회했습니다.\"";

        // 유저 아이디에 해당하는 장바구니 id없다면 서비스단에서 에러
        CartResponseDto responseDto = cartService.getCartDetails(userId);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, message, responseDto));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<Void>> updateCart(@RequestBody UpdateCartItemRequestDto requestDto) {
        // TODO JWT 토큰을 통해서 유저아이디 가져오고, 장바구니 있는지 조회 필요
        // TODO JWT토큰 가져오면 이거 삭제해야함, 더미 유저있다고 가정
        Long userId = 1L;
        String message = "\"수정한 메뉴가 장바구니에 반영되었습니다.\"";

        cartService.updateCart(userId, requestDto);
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse<Void>> deleteCartItem(@PathVariable Long cartId) {
        cartService.deleteCartItem(cartId);
        String message = "\"해당 메뉴가 장바구니에서 삭제되었습니다.\"";
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    @DeleteMapping("/all")
    public ResponseEntity<ApiResponse<Void>> clearCart() {
        // TODO id는 헤더에서 가져오기
        Long userId = 1L;
        cartService.clearCart(userId);
        String message = "\"장바구니가 삭제되었습니다.\"";
        return ResponseEntity.ok(ApiResponse.success(message));
    }
}
