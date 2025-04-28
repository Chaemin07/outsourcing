package com.example.outsourcing.payment.service;

import com.example.outsourcing.common.exception.BaseException;
import com.example.outsourcing.common.exception.ErrorCode;
import com.example.outsourcing.order.entity.Order;
import com.example.outsourcing.order.repository.OrderRepository;
import com.example.outsourcing.order.service.OrderService;
import com.example.outsourcing.payment.dto.PaymentRequestDto;
import com.example.outsourcing.payment.dto.PaymentResponseDto;
import com.example.outsourcing.payment.entity.Payment;
import com.example.outsourcing.common.enums.PaymentMethod;
import com.example.outsourcing.common.enums.PaymentStatus;
import com.example.outsourcing.payment.exception.InsufficientBalanceException;
import com.example.outsourcing.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentService {
    private final OrderService orderService;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public PaymentResponseDto createPayment(PaymentRequestDto requestDto) {
        Order order = orderRepository.findById(requestDto.getOrderId())
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_ORDER_ID));
        Integer orderPrice = orderService.calculateTotalPrice(order);

        // 주문 금액 > 현금
        int difference = orderPrice - requestDto.getInputAmount();
        if (difference>0) {
            throw new BaseException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        Payment payment = Payment.builder()
                .totalPrice(orderPrice)
                .method(requestDto.getPaymentMethod())
                .status(PaymentStatus.COMPLETED)
                .order(order)
                .build();

        order.setPayment(payment);

        Payment savedPayment = paymentRepository.save(payment);
        // 현금 결제일 경우만 remainingBalance 포함
        if (requestDto.getPaymentMethod() == PaymentMethod.CASH) {
            return PaymentResponseDto.builder()
                    .paymentId(savedPayment.getId())
                    .paymentStatus(PaymentStatus.COMPLETED)
                    .paymentTime(savedPayment.getCreatedAt())
                    .remainingBalance(difference) // 현금 결제: 잔액 포함
                    .build();
        }

        // 카드 결제: 잔액 없음
        return PaymentResponseDto.builder()
                .paymentId(savedPayment.getId())
                .paymentStatus(PaymentStatus.COMPLETED)
                .paymentTime(savedPayment.getCreatedAt())
                .build();
    }

    @Transactional
    public PaymentResponseDto cancelPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_PAYMENT_ID));
        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new BaseException(ErrorCode.INVALID_PAYMENT_STATUS_FOR_CANCEL);
        }
        // 결제 취소 처리
        payment.cancel();

        // 환불 처리 (현금 결제일 경우)
        Integer refundAmount = null;
        paymentRepository.save(payment);

        if (payment.getMethod() == PaymentMethod.CASH) {
            refundAmount = payment.getTotalPrice(); // 환불 금액
        }
        // 응답 생성
        PaymentResponseDto.PaymentResponseDtoBuilder responseBuilder = PaymentResponseDto.builder()
                .paymentId(payment.getId())
                .paymentStatus(PaymentStatus.CANCELED)
                .paymentTime(payment.getCreatedAt())
                .cancelTime(payment.getCancelTime());

        if (refundAmount != null) {
            responseBuilder.remainingBalance(refundAmount); // 환불 후 잔액
        }

        return responseBuilder.build();
    }
}
