package com.example.outsourcing.order.dto;

import com.example.outsourcing.order.entity.DeliveryStatus;
import com.example.outsourcing.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Builder
public class StoreOrderResponseDto {
    private Long orderId;
    private OrderStatus orderStatus;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime createdAt;

    // 주문 고객 정보
    private String customerName;
    private String customerPhone;
    private String deliveryAddress;
    private String requestMessage;


    private List<StoreOrderDetailDto> orderDetailList;

    // 총 금액
    private Integer totalPrice;


}
