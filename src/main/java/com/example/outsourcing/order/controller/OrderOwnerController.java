package com.example.outsourcing.order.controller;

import com.example.outsourcing.common.annotation.AuthUser;
import com.example.outsourcing.common.enums.DeliveryStatus;
import com.example.outsourcing.common.enums.OrderStatus;
import com.example.outsourcing.common.response.ApiResponse;
import com.example.outsourcing.order.dto.*;
import com.example.outsourcing.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.outsourcing.common.enums.DeliveryStatus.PREPARED;
import static com.example.outsourcing.common.enums.OrderStatus.COMPLETED;


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
        List<StoreOrderResponseDto> orderList = orderService.getOrdersByStoreId(userId, storeId);
        String message = "가게 주문 리스트입니다.";
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,message,orderList));

    }

    // [사장님] 주문 상태 변경
    @PatchMapping("/stores/{storeId}/orders/{orderId}/order-status")
    public ResponseEntity<ApiResponse<OrderStatusChangeResponse>> updateOrderStatus(
            @AuthUser Long userId,
            @PathVariable Long storeId,
            @PathVariable Long orderId,
            @RequestBody @Valid OrderStatusUpdateRequest request) {
        // 컨트롤러에서 DTO 생성
        StatusChangeDto dto = new StatusChangeDto(
                storeId,
                orderId,
                OrderStatus.valueOf(request.getStatus().toUpperCase()),
                PREPARED // 배달 상태는 null
        );

        OrderStatusChangeResponse responseDto = orderService.updateOrderStatus(userId, dto );
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

        // 컨트롤러에서 DTO 생성
        StatusChangeDto dto = new StatusChangeDto(
                storeId,
                orderId,
                COMPLETED, // 주문 상태는 null
                DeliveryStatus.valueOf(request.getDeliveryStatus().toUpperCase())
        );
        DeliveryStatusChangeResponse responseDto = orderService.updateDeliveryStatus(userId, dto);
        String message = "배달 상태가 변경되었습니다.";
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,message,responseDto));
    }
}
