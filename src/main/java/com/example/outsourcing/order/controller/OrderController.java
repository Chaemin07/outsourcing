package com.example.outsourcing.order.controller;

import com.example.outsourcing.common.response.ApiResponse;
import com.example.outsourcing.order.dto.OrderRequestDto;
import com.example.outsourcing.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    // ############################사용자############################
    //주문 생성
    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<?>> createOrder(@RequestBody OrderRequestDto requestDto) {
        // TODO 사용자는 헤더에서 가져와야함
        Long userId = 1L;
        String successMessage = "\"주문이 성공적으로 접수되었습니다.\"";


        orderService.createOrder(userId, requestDto);
        return ResponseEntity.ok(ApiResponse.success("성공적!"));
    }

    // [사용자] 주문 단건 조회
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<?>> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(ApiResponse.success("성공적!"));
    }
    // [사용자]내 전체 주문 목록(내역) 조회
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<?>> getOrders(@PathVariable Long orderId) {
        return ResponseEntity.ok(ApiResponse.success("성공적!"));
    }


    // ############################사장님############################
    
}
