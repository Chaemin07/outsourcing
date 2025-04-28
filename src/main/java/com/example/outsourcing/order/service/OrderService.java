package com.example.outsourcing.order.service;

import com.example.outsourcing.common.annotation.OrderStatusLogTarget;
import com.example.outsourcing.common.enums.DeliveryStatus;
import com.example.outsourcing.common.enums.OrderStatus;
import com.example.outsourcing.menu.entity.Menu;
import com.example.outsourcing.menu.entity.MenuOption;
import com.example.outsourcing.menu.repository.MenuOptionRepository;
import com.example.outsourcing.menu.repository.MenuRepository;
import com.example.outsourcing.order.dto.*;
import com.example.outsourcing.order.entity.*;
import com.example.outsourcing.order.repository.OrderDetailRepository;
import com.example.outsourcing.order.repository.OrderItemOptionRepository;
import com.example.outsourcing.order.repository.OrderRepository;
import com.example.outsourcing.store.entity.Store;
import com.example.outsourcing.store.repository.StoreRepository;
import com.example.outsourcing.user.entity.Role;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final MenuOptionRepository manuOptionRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderItemOptionRepository orderItemOptionRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public OrderResponseDto createOrder(Long userId, OrderRequestDto requestDto) {
        // TODO 캐시로 가게별 주문 최소 금액 가져오기
        Integer storeMinOrderPrice = 10000;
        String returnMessage = "";
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Long menuId = requestDto.getOrderItems().get(0).getMenuId();
        // 가게(store)를 찾기 위해서 menu생성
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다!"));
        // TODO 가게 상태에 따라 주문 가능 여부 판단
        Store store = menu.getStore();
        String requestMessage = requestDto.getRequestMessage();
        String deliveryAddress = requestDto.getDeliveryAddress();
        List<OrderItemRequestDto> orderItems = requestDto.getOrderItems();
        // 주문금액 계산
        Integer orderPrice = orderPriceCheck(orderItems);
        // 주문 최소 금액 미만의 주문 금액
        if (storeMinOrderPrice > orderPrice) {
            returnMessage = (storeMinOrderPrice - orderPrice) + "원 더 담으면 주문 가능! 메뉴를 추가해주세요!";
            throw new RuntimeException(returnMessage);
        }
        // 주문 생성
        Order newOrder = Order.builder()
                .requestMessage(requestMessage)
                .deliveryAddress(deliveryAddress)
                .orderStatus(OrderStatus.PENDING)
                .deliveryStatus(DeliveryStatus.PREPARED)
                .user(user)
                .store(store)
                .build();
        Order savedOrder = orderRepository.save(newOrder);
        if (savedOrder.getId() == null) {
            throw new RuntimeException("저장실패!");
        }
        // 주문 상세 생성
        for (OrderItemRequestDto orderItem : orderItems) {
            Long itemMenuId = orderItem.getMenuId();
            Menu menuItem = menuRepository.findById(itemMenuId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다!"));
            OrderDetail orderDetail = OrderDetail.builder()
                    .menuName(menuItem.getName())
                    .price(menuItem.getPrice())
                    .order(newOrder)
                    .menu(menuItem)
                    .build();
            // 주문 상세 저장
            OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);
            // 주문 옵션있다면 생성
            if (orderItem.getOptionIds() != null) {
                for (Long optionId : orderItem.getOptionIds()) {
                    MenuOption menuOption = manuOptionRepository.findById(optionId)
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴 옵션입니다!"));
                    OrderItemOption itemOption = OrderItemOption.builder()
                            .optionPrice(menuOption.getPrice())
                            .orderDetail(savedOrderDetail)
                            .menu(menuItem)
                            .menuOption(menuOption)
                            .build();

                    // 주문 옵션 저장
//                    orderItemOptionRepository.save(itemOption);
                    // 더티체킹으로 주문 옵션 저장
                    savedOrderDetail.addOrderItemOption(itemOption);
                }
            }

        }
        // 주문 잘 저장되었는지 체크 필요
        returnMessage = "주문이 성공적으로 완료되었습니다.";
        OrderResponseDto responseDto = OrderResponseDto.builder()
                .orderId(savedOrder.getId())
                .status(OrderStatus.PENDING.getValue())
                .totalPrice(orderPrice)
                .createdAt(savedOrder.getCreatedAt())
                .updatedAt(savedOrder.getUpdatedAt())
                .build();
        return responseDto;

    }

    // 주문금액 계산 메서드
    private Integer orderPriceCheck(List<OrderItemRequestDto> orderItems) {
        Integer totalPrice = 0;
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

    // 주문 계산
    public Integer calculateTotalPrice(Order order) {
        Integer totalPrice = 0;
        for (OrderDetail orderDetail : order.getOrderDetailList()) {
            Integer menuPrice = orderDetail.getPrice();
            Integer optionTotalPrice = 0;

            for (OrderItemOption orderItemOption : orderDetail.getOrderItemOptionList()) {
                optionTotalPrice += orderItemOption.getOptionPrice();
            }
            totalPrice += menuPrice + optionTotalPrice;
        }
        return totalPrice;
    }

    public OrderResponseDto getOrderById(Long userId, Long orderId) {
        // 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다!"));
        // 주문id와 사용자id 비교
        if (!order.getUser().getId().equals(userId)) {
            // TODO 권한 에러 throw
            throw new RuntimeException("다른 사용자의 주문에 접근할 수 없습니다!");
        }
        return OrderResponseDto.builder()
                .orderId(order.getId())
                .status(order.getOrderStatus().getValue())
                .totalPrice(calculateTotalPrice(order))
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    public List<OrderResponseDto> getOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderResponseDto> orderResponseDtoList = new ArrayList<>();

        for (Order order : orders) {
            Integer totalPrice = calculateTotalPrice(order);
            OrderResponseDto responseDto = OrderResponseDto.builder()
                    .orderId(order.getId())
                    .status(order.getOrderStatus().getValue())
                    .totalPrice(totalPrice)
                    .createdAt(order.getCreatedAt())
                    .updatedAt(order.getUpdatedAt())
                    .build();
            orderResponseDtoList.add(responseDto);
        }
        return orderResponseDtoList;
    }

    @Transactional
    public void cancelOrderByUser(Long userId, Long orderId, String cancelReason) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다!"));
        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("다른 사용자의 주문에 접근할 수 없습니다!");
        }
        // 이미 취소했거나 거절된 주문 상태인 경우
        if (order.getOrderStatus() == OrderStatus.CANCELLED ||
                order.getOrderStatus() == OrderStatus.REJECTED) {
            throw new RuntimeException("주문 상태를 변경할 수 없습니다!");
        }
        // 주문 접수 전인 대기 상태에만 사용자 주문 취소 가능
        if (order.getOrderStatus() == OrderStatus.PENDING) {
            order.setOrderStatus(OrderStatus.CANCELLED);
            order.setCanceledBy(user.getRole());
            order.setCancelReason(cancelReason);
        }
    }

    public List<StoreOrderResponseDto> getOrdersByStoreId(Long userId, Long storeId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 가게입니다!"));
        // 일반 사용자이거나 가게 주인이 아닌경우
        if (owner.getRole() == Role.USER || !store.getUser().getId().equals(owner.getId())) {
            throw new RuntimeException("해당 가게에 대한 권한이 없습니다.");
        }

        List<Order> orderList = orderRepository.findByStoreId(storeId);
        return convertOrdersToDto(orderList);
    }
    // StoreOrderResponseDto 변환 메서드
    private List<StoreOrderResponseDto> convertOrdersToDto(List<Order> orders) {
        List<StoreOrderResponseDto> responseDtos = new ArrayList<>();

        for (Order order : orders) {
            // 주문 금액 계산
            int totalPrice = calculateTotalPrice(order);

            StoreOrderResponseDto dto = StoreOrderResponseDto.builder()
                    .orderId(order.getId())
                    .orderStatus(order.getOrderStatus())
                    .deliveryStatus(order.getDeliveryStatus())
                    .createdAt(order.getCreatedAt())
                    .customerName(order.getUser().getName())
                    .customerPhone(order.getUser().getPhoneNumber())
                    .deliveryAddress(order.getDeliveryAddress())
                    .requestMessage(order.getRequestMessage())
                    .orderDetailList(convertOrderDetails(order.getOrderDetailList())) // 주문 상세 변환
                    .totalPrice(totalPrice)
                    .build();

            responseDtos.add(dto);
        }

        return responseDtos;
    }
    
    // StoreOrderDetailDto 주문 상세 정보 처리 메서드
    private List<StoreOrderDetailDto> convertOrderDetails(List<OrderDetail> orderDetailList) {
        Map<String, StoreOrderDetailDto> groupedDetails = new HashMap<>();
        StringBuilder menuKey;
        // 주문 상세 - 주문 메뉴 전체 조회
        for (OrderDetail orderDetail : orderDetailList) {
            String menuName = orderDetail.getMenuName();
            // 정렬 안하면 순서에 따라서 같은 내용 다른 key가 될수도 있음 → (메뉴+옵션리스트)가 하나의 key임
            List<String> sortedOptionNameList = orderDetail.getOrderItemOptionList().stream()
                    .map(option -> option.getMenuOption().getOptionName())
                    .sorted()
                    .collect(Collectors.toList());
            menuKey = new StringBuilder();
            menuKey.append(menuName).append(" "+sortedOptionNameList.toString());
            
            // 메뉴 옵션 Dto 변환
            List<StoreOrderOptionDto> storeOrderOptionDtoList = new ArrayList<>();
            for (OrderItemOption orderItemOption : orderDetail.getOrderItemOptionList()) {
                StoreOrderOptionDto orderOptionDto = StoreOrderOptionDto.builder()
                        .optionName(orderItemOption.getMenuOption().getOptionName())
                        .optionPrice(orderItemOption.getOptionPrice())
                        .build();
                storeOrderOptionDtoList.add(orderOptionDto);
            }

            if (groupedDetails.containsKey(menuKey)) {
                StoreOrderDetailDto storeOrderDetailDto = groupedDetails.get(menuKey);
                int updatedQuantity = storeOrderDetailDto.getQuantity() + 1;

                groupedDetails.put(menuKey.toString(), StoreOrderDetailDto.builder()
                        .menuName(orderDetail.getMenuName())
                        .menuPrice(orderDetail.getPrice())
                        .quantity(updatedQuantity)
                        .build());
            }else{
                groupedDetails.put(menuKey.toString(), StoreOrderDetailDto.builder()
                        .menuName(orderDetail.getMenuName())
                        .menuPrice(orderDetail.getPrice())
                        .quantity(1)
                        .build());
            }
        }
        // 최종 결과: groupedDetails(Map) value만 리스트로 변환해서 리턴
        return  new ArrayList<>(groupedDetails.values());
    }

    @OrderStatusLogTarget
    @Transactional
    public OrderStatusChangeResponse updateOrderStatus(Long ownerId, StatusChangeDto statusChangeDto) {
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        // 가게 아이디를 통해서 store -> 가게 주인과 ownerId같아야함
        Store store = storeRepository.findById(statusChangeDto.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 가게입니다!"));
        // 사장님 role아니거나 가게주인과 ownerId다르다면 throw
        if (user.getRole()!=Role.OWNER||!store.getUser().getId().equals(ownerId)){
            throw new RuntimeException("해당 가게에 대한 권한이 없습니다.");
        }
        // 주문 아이디를 통해서 order -> 주문한 가게와 storeId같아야함
        Order order = orderRepository.findById(statusChangeDto.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다!"));
        // 주문한 가게의 Id와 storeId가 다르다면 throw
        if (!order.getStore().getId().equals(statusChangeDto.getStoreId())) {
            throw new RuntimeException("해당 가게에 대한 권한이 없습니다.");
        }
        String previousOrderStatus = order.getOrderStatus().getValue();
        try {
            // 입력받은 상태값 체크
            OrderStatus orderStatus = statusChangeDto.getOrderStatus();
            order.setOrderStatus(orderStatus);
        } catch (IllegalArgumentException error) {
            throw new IllegalArgumentException("잘못된 상태가 입력되었습니다! : " + statusChangeDto.getOrderStatus());
        }

        return OrderStatusChangeResponse.builder()
                .orderId(statusChangeDto.getOrderId())
                .previousOrderStatus(previousOrderStatus)
                .newOrderStatus(order.getOrderStatus().getValue())
                .build();
    }

    @OrderStatusLogTarget
    @Transactional
    public DeliveryStatusChangeResponse updateDeliveryStatus(Long ownerId, StatusChangeDto statusChangeDto) {
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        // 가게 아이디를 통해서 store -> 가게 주인과 ownerId같아야함
        Store store = storeRepository.findById(statusChangeDto.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 가게입니다!"));
        // 사장님 role아니거나 가게주인과 ownerId다르다면 throw
        if (user.getRole()!=Role.OWNER||!store.getUser().getId().equals(ownerId)){
            throw new RuntimeException("해당 가게에 대한 권한이 없습니다.");
        }
        // 주문 아이디를 통해서 order -> 주문한 가게와 storeId같아야함
        Order order = orderRepository.findById(statusChangeDto.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다!"));
        // 주문한 가게의 Id와 storeId가 다르다면 throw
        if (!order.getStore().getId().equals(statusChangeDto.getStoreId())) {
            throw new RuntimeException("해당 가게에 대한 권한이 없습니다.");
        }
        String previousDeliveryStatus = order.getDeliveryStatus().getValue();
        try {
            // 입력받은 상태값 체크
            DeliveryStatus deliveryStatus = statusChangeDto.getDeliveryStatus();
            order.setDeliveryStatus(deliveryStatus);
        } catch (IllegalArgumentException error) {
            throw new IllegalArgumentException("잘못된 상태가 입력되었습니다! : " + statusChangeDto.getDeliveryStatus());
        }

        return DeliveryStatusChangeResponse.builder()
                .orderId(statusChangeDto.getOrderId())
                .previousDeliveryStatus(previousDeliveryStatus)
                .newDeliveryStatus(order.getDeliveryStatus().getValue())
                .build();

    }

}
