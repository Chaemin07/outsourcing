package com.example.outsourcing.order.service;

import com.example.outsourcing.common.enums.DeliveryStatus;
import com.example.outsourcing.common.enums.OrderStatus;
import com.example.outsourcing.menu.entity.Menu;
import com.example.outsourcing.menu.entity.MenuOption;
import com.example.outsourcing.order.dto.DeliveryStatusChangeResponse;
import com.example.outsourcing.order.dto.OrderResponseDto;
import com.example.outsourcing.order.dto.OrderStatusChangeResponse;
import com.example.outsourcing.order.dto.StatusChangeDto;
import com.example.outsourcing.order.entity.Order;
import com.example.outsourcing.order.entity.OrderDetail;
import com.example.outsourcing.order.entity.OrderItemOption;
import com.example.outsourcing.order.repository.OrderRepository;
import com.example.outsourcing.store.entity.Store;
import com.example.outsourcing.store.repository.StoreRepository;
import com.example.outsourcing.user.entity.Role;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder() {
    }

    @DisplayName("주문 금액이 정확히 계산되는지 검증")
    @Test
    void calculateTotalPrice() {
        Menu 메인메뉴 = Menu.builder()
                .id(1L)
                .name("햄버거")
                .price(10000)
                .build();
        MenuOption 메뉴옵션1 = MenuOption.builder()
                .id(11L)
                .optionName("콜라")
                .price(2000)
                .build();
        MenuOption 메뉴옵션2 = MenuOption.builder()
                .id(12L)
                .optionName("감자튀김")
                .price(2000)
                .build();
        OrderItemOption 주문옵션항목1 = OrderItemOption.builder()
                .menuOption(메뉴옵션1)
                .optionPrice(메뉴옵션1.getPrice())
                .build();
        OrderItemOption 주문옵션항목2 = OrderItemOption.builder()
                .menuOption(메뉴옵션2)
                .optionPrice(메뉴옵션2.getPrice())
                .build();
        OrderDetail 주문상세1 = OrderDetail.builder()
                .menu(메인메뉴)
                .price(메인메뉴.getPrice())
                .orderItemOptionList(List.of(주문옵션항목1, 주문옵션항목2))
                .build();
        Order 주문 = Order.builder()
                .orderDetailList(List.of(주문상세1))
                .build();
        // when
        Integer totalPrice = orderService.calculateTotalPrice(주문);

        // then
        assertEquals(14000, totalPrice);
    }

    @DisplayName("사장님이 주문 상태를 성공적으로 변경")
    @Test
    void updateOrderStatus() {
        //given
        Long userId = 1L;
        Long storeId = 10L;
        Long orderId = 11L;

        StatusChangeDto statusChangeDto = StatusChangeDto.builder()
                .storeId(storeId)
                .orderId(orderId)
                .orderStatus(OrderStatus.ACCEPTED)
                .build();

        User owner = User.builder()
                .id(userId)
                .role(Role.OWNER)
                .build();
        Store store = Store.builder()
                .id(storeId)
                .user(owner)
                .build();
        Order order = Order.builder()
                .id(orderId)
                .store(store)
                .orderStatus(OrderStatus.PENDING)
                .build();

        // mocking
        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // when
        OrderStatusChangeResponse response = orderService.updateOrderStatus(userId, statusChangeDto);

        // then
        assertEquals(orderId, response.getOrderId());
        assertEquals(OrderStatus.PENDING.getValue(), response.getPreviousOrderStatus());
        assertEquals(OrderStatus.ACCEPTED.getValue(), response.getNewOrderStatus());

    }

    @DisplayName("사장님이 배달 상태를 성공적으로 변경")
    @Test
    void updateDeliveryStatus() {
        // given
        Long userId = 1L;
        Long storeId = 10L;
        Long orderId = 100L;

        StatusChangeDto statusChangeDto = StatusChangeDto.builder()
                .storeId(storeId)
                .orderId(orderId)
                .deliveryStatus(DeliveryStatus.DELIVERING)
                .build();

        User owner = User.builder()
                .id(userId)
                .role(Role.OWNER)
                .build();

        Store store = Store.builder()
                .id(storeId)
                .user(owner) // store의 주인은 owner로 설정
                .build();

        Order order = Order.builder()
                .id(orderId)
                .store(store)
                .deliveryStatus(DeliveryStatus.PREPARED) // 기존 상태: PREPARED
                .build();

        // mocking
        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // when
        DeliveryStatusChangeResponse response = orderService.updateDeliveryStatus(userId, statusChangeDto);

        // then
        assertEquals(orderId, response.getOrderId());
        assertEquals(DeliveryStatus.PREPARED.getValue(), response.getPreviousDeliveryStatus());
        assertEquals(DeliveryStatus.DELIVERING.getValue(), response.getNewDeliveryStatus());

    }

    @DisplayName("사용자가 자신의 주문 조회")
    @Test
    void getOrderById() {
        // given
        Long userId = 1L;
        Long orderId = 100L;

        User user = User.builder()
                .id(userId)
                .name("유비빔")
                .build();
        // 주문상세
        OrderDetail orderDetail = OrderDetail.builder()
                .price(15000) // 메뉴 가격
                .orderItemOptionList(List.of()) // 옵션 없음
                .build();

        Order order = Order.builder()
                .id(orderId)
                .user(user)
                .orderStatus(OrderStatus.PENDING)
                .orderDetailList(List.of(orderDetail))
                .build();

        // mocking
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // when
        OrderResponseDto responseDto = orderService.getOrderById(userId, orderId);

        // then
        assertEquals(orderId, responseDto.getOrderId());
        assertEquals(OrderStatus.PENDING.getValue(), responseDto.getStatus());
        assertEquals(15000, responseDto.getTotalPrice());
    }

}