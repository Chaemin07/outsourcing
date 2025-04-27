package com.example.outsourcing.payment.controller;

import com.example.outsourcing.common.response.ApiResponse;
import com.example.outsourcing.payment.dto.PaymentRequestDto;
import com.example.outsourcing.payment.dto.PaymentResponseDto;
import com.example.outsourcing.payment.entity.Payment;
import com.example.outsourcing.payment.repository.PaymentRepository;
import com.example.outsourcing.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/payments")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> createPayment(@RequestBody PaymentRequestDto requestDto) {
        PaymentResponseDto responseDto = paymentService.createPayment(requestDto);
        String message = "주문이 결제되었습니다!";
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, message, responseDto));
    }

    @PostMapping("/payments/{paymentId}/cancel")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> cancelPayment(@PathVariable Long paymentId) {
        PaymentResponseDto responseDto = paymentService.cancelPayment(paymentId);
        String message = "주문이 취소되었습니다!";
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, message, responseDto));

    }
}
