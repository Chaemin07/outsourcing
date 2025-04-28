package com.example.outsourcing.common.aop.log;

import com.example.outsourcing.common.annotation.OrderStatusLogTarget;
import com.example.outsourcing.common.enums.DeliveryStatus;
import com.example.outsourcing.common.enums.OrderStatus;
import com.example.outsourcing.order.dto.StatusChangeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OrderStatusLogger {

    // AOP가 적용될 대상 메서드
    @Pointcut("@annotation(com.example.outsourcing.common.annotation.OrderStatusLogTarget)")
    public void statusLogPointcut() {}

    @Around("statusLogPointcut()")
    public Object logStatusChange(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime requestTime = LocalDateTime.now();
        Long storeId = null;
        Long orderId = null;
        OrderStatus orderStatus = null;
        DeliveryStatus deliveryStatus = null;

        //대상 메서드에 전달된 파라미터 목록을 배열로 반환
        for (Object arg : joinPoint.getArgs()) {
            // StatusChangeDto dto로 변환
            if (arg instanceof StatusChangeDto dto) {
                storeId = dto.getStoreId();
                orderId = dto.getOrderId();
                orderStatus = dto.getOrderStatus();
                deliveryStatus = dto.getDeliveryStatus();
            }
        }
        // 비즈니스 로직 실행
        Object result = joinPoint.proceed();

        // 정확한 조건 로깅
        if (orderStatus == OrderStatus.COMPLETED) {
            log.info("배달 상태 로깅 - 요청 시각: {}, 가게 id: {}, 주문 id: {}, 배달 상태: {}",
                    requestTime, storeId, orderId, deliveryStatus);
        } else if (deliveryStatus == DeliveryStatus.PREPARED) {
            log.info("주문 상태 로깅 - 요청 시각: {}, 가게 id: {}, 주문 id: {}, 주문 상태: {}",
                    requestTime, storeId, orderId, orderStatus);
        }

        return result;
    }
}
