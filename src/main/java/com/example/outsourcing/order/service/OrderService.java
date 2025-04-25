package com.example.outsourcing.order.service;

import com.example.outsourcing.menu.entity.Menu;
import com.example.outsourcing.menu.entity.MenuOption;
import com.example.outsourcing.menu.repository.ManuOptionRepository;
import com.example.outsourcing.menu.repository.MenuRepository;
import com.example.outsourcing.order.dto.OrderItemRequestDto;
import com.example.outsourcing.order.dto.OrderRequestDto;
import com.example.outsourcing.order.entity.DeliveryStatus;
import com.example.outsourcing.order.entity.Order;
import com.example.outsourcing.order.entity.OrderStatus;
import com.example.outsourcing.order.repository.OrderRepository;
import com.example.outsourcing.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final ManuOptionRepository manuOptionRepository;

    public String createOrder(Long userId,OrderRequestDto requestDto) {
        // TODO 캐시로 가게별 주문 최소 금액 가져오기
        Integer storeMinOrderPrice= 10000;
        String returnMessage = "";

        Long menuId = requestDto.getOrderItems().get(0).getMenuId();
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다!"));
        Store store = menu.getStore();
        String requestMessage = requestDto.getRequestMessage();
        String deliveryAddress = requestDto.getDeliveryAddress();
        List<OrderItemRequestDto> orderItems = requestDto.getOrderItems();
        Integer orderPrice = orderPriceCheck(orderItems);
        // 주문 최소 금액 미만의 주문 금액
        if (storeMinOrderPrice > orderPrice) {
            returnMessage = (storeMinOrderPrice - orderPrice) + "원 더 담으면 주문 가능!";
            return returnMessage;
        }
        Order build = Order.builder()
                .requestMessage(requestMessage)
                .deliveryAddress(deliveryAddress)
                .orderStatus(OrderStatus.PENDING)
                .deliveryStatus(DeliveryStatus.PREPARED)
//                .user TODO user랑 가게
                .build();

//        orderRepository.save()

        return returnMessage;

    }

    private Integer orderPriceCheck(List<OrderItemRequestDto> orderItems) {
        Integer totalPrice=0;
        for (OrderItemRequestDto orderItem : orderItems) {
            // TODO 장바구니에 없어도 주문 가능하도록 (throw 처리 고민)
            // 단, 메뉴 DB에는 해당 메뉴가 있어야함
            Optional<Menu> optionalMenu = menuRepository.findById(orderItem.getMenuId());
            if (optionalMenu.isPresent()) {
                totalPrice += optionalMenu.get().getPrice() * orderItem.getQuantity();
            }
            Integer optionTotalPrice = 0;
            //
            for (Long optionId : orderItem.getOptionIds()) {
                Optional<MenuOption> optionalMenuOption = manuOptionRepository.findById(optionId);
                if (optionalMenuOption.isPresent()) {
                    optionTotalPrice += optionalMenuOption.get().getPrice();
                }
            }
        }
        return totalPrice;
    }
}
