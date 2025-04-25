package com.example.outsourcing.order.controller;

import com.example.outsourcing.common.annotation.AuthUser;
import com.example.outsourcing.common.response.ApiResponse;
import com.example.outsourcing.order.dto.CancelUserRequestDto;
import com.example.outsourcing.order.dto.OrderRequestDto;
import com.example.outsourcing.order.dto.OrderResponseDto;
import com.example.outsourcing.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    // ############################사용자############################
    //주문 생성
    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(
            @AuthUser Long userId,
            @RequestBody OrderRequestDto requestDto) {
        // TODO 사용자는 헤더에서 가져와야함
        OrderResponseDto responseDto = orderService.createOrder(userId, requestDto);
        String successMessage = "주문이 생성되었습니다.";
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, successMessage, responseDto));
    }

    // [사용자] 주문 단건 조회
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<?>> getOrderById(
            @AuthUser Long userId,
            @PathVariable Long orderId) {
        OrderResponseDto responseDto = orderService.getOrderById(userId, orderId);
        String message = "주문을 조회했습니다.";
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, message, responseDto));
    }

    // [사용자]내 전체 주문 목록(내역) 조회
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> getOrders(@AuthUser Long userId) {
        List<OrderResponseDto> orders = orderService.getOrders(userId);
        String message = "주문 내역을 조회했습니다.";
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, message, orders));
    }

    // [사용자] 주문 취소
    @PatchMapping("/orders/{orderId}/cancel")
    public ResponseEntity<?> cancelOrderByUser(
            @AuthUser Long userId,
            @PathVariable Long orderId,
            @RequestBody CancelUserRequestDto requestDto
    ) {
        orderService.cancelOrderByUser(userId, orderId, requestDto.getMessage());
        String message = "주문이 성공적으로 취소되었습니다.";
        return ResponseEntity.ok(ApiResponse.success(message));
    }


    // ############################사장님############################
    //

}
