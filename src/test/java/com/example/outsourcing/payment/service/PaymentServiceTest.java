package com.example.outsourcing.payment.service;

import com.example.outsourcing.common.enums.PaymentMethod;
import com.example.outsourcing.common.enums.PaymentStatus;
import com.example.outsourcing.order.entity.Order;
import com.example.outsourcing.order.repository.OrderRepository;
import com.example.outsourcing.order.service.OrderService;
import com.example.outsourcing.payment.dto.PaymentRequestDto;
import com.example.outsourcing.payment.dto.PaymentResponseDto;
import com.example.outsourcing.payment.entity.Payment;
import com.example.outsourcing.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {


    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderService orderService;

    private PaymentRequestDto cashRequestDto;
    private Order mockOrder;

    @BeforeEach
    void setUp() {
        // 더미 주문
        mockOrder = Order.builder().id(1L).build();

        // 현금 결제 요청
        cashRequestDto = PaymentRequestDto.builder()
                .orderId(1L)
                .inputAmount(12000) // 현금 12,000원
                .paymentMethod(PaymentMethod.CASH)
                .build();
    }

    @DisplayName("현금 결제 생성 성공 시 잔액 포함 응답 반환")
    @Test
    void createPaymentWithCashSuccess() {
        // given
        Integer totalPrice = 10000; // 주문 금액
        Integer inputAmount = 12000; // 현금 입력 금액
        cashRequestDto = PaymentRequestDto.builder()
                .orderId(1L)
                .inputAmount(inputAmount)
                .paymentMethod(PaymentMethod.CASH)
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));
        when(orderService.calculateTotalPrice(mockOrder)).thenReturn(totalPrice);

        Payment savedPayment = Payment.builder()
                .id(10L)
                .method(PaymentMethod.CASH)
                .status(PaymentStatus.COMPLETED)
                .totalPrice(totalPrice)
                .order(mockOrder)
                .build();

        when(paymentRepository.save(ArgumentMatchers.<Payment>any())).thenReturn(savedPayment);

        // when
        PaymentResponseDto result = paymentService.createPayment(cashRequestDto);

        // then
        assertEquals(PaymentStatus.COMPLETED, result.getPaymentStatus());
        assertEquals(2000, result.getRemainingBalance()); // 12000 - 10000 = 2000
        verify(paymentRepository, times(1)).save(ArgumentMatchers.<Payment>any());
    }


    @DisplayName("카드 결제 생성 성공 시 응답 반환")
    @Test
    void createPaymentWithCardSuccess() {
        // given
        Integer totalPrice = 10000; // 주문 금액
        cashRequestDto = PaymentRequestDto.builder()
                .orderId(1L)
                .inputAmount(10000) // 카드 결제는 정확히 맞춰서 낼 수도 있음
                .paymentMethod(PaymentMethod.CARD)
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));
        when(orderService.calculateTotalPrice(mockOrder)).thenReturn(totalPrice);

        Payment savedPayment = Payment.builder()
                .id(10L)
                .method(PaymentMethod.CARD)
                .status(PaymentStatus.COMPLETED)
                .totalPrice(totalPrice)
                .order(mockOrder)
                .build();

        when(paymentRepository.save(ArgumentMatchers.<Payment>any())).thenReturn(savedPayment);

        // when
        PaymentResponseDto result = paymentService.createPayment(cashRequestDto);

        // then
        assertEquals(PaymentStatus.COMPLETED, result.getPaymentStatus());
        assertNull(result.getRemainingBalance()); // 카드 결제는 잔액 없음
        verify(paymentRepository, times(1)).save(ArgumentMatchers.<Payment>any());
    }


    @DisplayName("현금 결제 취소 성공 - 환불 금액 반환")
    @Test
    void cancelCashPaymentSuccess() {
        // given
        Long paymentId = 2L;

        Payment payment = Payment.builder()
                .id(paymentId)
                .status(PaymentStatus.COMPLETED)
                .method(PaymentMethod.CASH)
                .totalPrice(15000)
                .build();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        // when
        PaymentResponseDto result = paymentService.cancelPayment(paymentId);

        // then
        // 결제 상태가 취소로 변경되어야함
        assertEquals(PaymentStatus.CANCELED, result.getPaymentStatus());
        assertEquals(15000, result.getRemainingBalance()); // 환불 금액 = 결제 금액
        verify(paymentRepository, times(1)).save(payment);
    }

}