package com.example.outsourcing.order.controller;

import com.example.outsourcing.common.annotation.AuthUser;
import com.example.outsourcing.common.response.ApiResponse;
import com.example.outsourcing.order.dto.*;
import com.example.outsourcing.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class OrderOwnerController {
    private final OrderService orderService;

    // ############################사장님############################
    // [사장님] 가게별 전체 주문 목록 조회
    @PutMapping("/owners/stores/{storeId}/orders")
    public ResponseEntity<ApiResponse<List<StoreOrderResponseDto>>> getOrdersByStore(
            @AuthUser Long userId,
            @PathVariable Long storeId) {
        orderService.getOrdersByStoreId(userId, storeId);
        return null;
    }

    // [사장님] 주문 상태 변경
    @PatchMapping("/stores/{storeId}/orders/{orderId}/order-status")
    public ResponseEntity<ApiResponse<OrderStatusChangeResponse>> updateOrderStatus(
            @AuthUser Long userId,
            @PathVariable Long storeId,
            @PathVariable Long orderId,
            @RequestBody @Valid OrderStatusUpdateRequest request) {

        OrderStatusChangeResponse responseDto = orderService.updateOrderStatus(userId, storeId, orderId, request.getStatus());
        String message = "주문 상태가 변경되었습니다.";
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,message,responseDto));
    }

    // [사장님] 주문 배달 상태 변경
    @PatchMapping("/stores/{storeId}/orders/{orderId}/delivery-status")
    public ResponseEntity<ApiResponse<DeliveryStatusChangeResponse>> updateDeliveryStatus(
            @AuthUser Long userId,
            @PathVariable Long storeId,
            @PathVariable Long orderId,
            @RequestBody @Valid DeliveryStatusUpdateRequest request) {

        DeliveryStatusChangeResponse responseDto = orderService.updateDeliveryStatus(userId, storeId, orderId, request.getDeliveryStatus());
        String message = "배달 상태가 변경되었습니다.";
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,message,responseDto));
    }
}
