package com.example.outsourcing.cart.controller;


import com.example.outsourcing.cart.dto.CartRequestDto;
import com.example.outsourcing.cart.dto.CartResponseDto;
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
    public ResponseEntity<?> addItemtoCart(@RequestBody CartRequestDto requestDto) {
        String message = "\"장바구니에 메뉴가 추가되었습니다.\"";

        cartService.addItemtoCart(requestDto);
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Cart>> getCartItems() {
        // TODO JWT 토큰을 통해서 유저아이디 가져오고, 장바구니 있는지 조회 필요
        // TODO JWT토큰 가져오면 이거 삭제해야함, 더미 유저있다고 가정
        Long userId = 1L;
        // TODO 컨트롤러에서 장바구니 엔티티를 가져다가 리턴해준다? NONO 아닌거 같음
        String message = "\"장바구니를 조회했습니다.\"";

        //
        Cart cartByUserId = cartService.findCartByUserId(userId);
        // TODO Return부분에 Cart-> CartResponseDto로 변환필요
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, message, cartByUserId));
//        return ResponseEntity.ok(ApiResponse.error(HttpStatus.BAD_REQUEST, CartResponseDto));
    }

}
