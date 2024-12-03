package com.sparta.msa_exam.order.service;


import com.sparta.msa_exam.order.client.ProductClient;
import com.sparta.msa_exam.order.client.ProductResponseDto;
import com.sparta.msa_exam.order.domain.Order;
import com.sparta.msa_exam.order.domain.OrderProduct;
import com.sparta.msa_exam.order.domain.OrderStatus;
import com.sparta.msa_exam.order.dto.request.OrderProductRequestDto;
import com.sparta.msa_exam.order.dto.request.OrderRequestDto;
import com.sparta.msa_exam.order.dto.response.OrderResponseDto;
import com.sparta.msa_exam.order.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    @Transactional
    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackCreateOrder")
    public OrderResponseDto createOrder(OrderRequestDto requestDto, String userId) {

        checkProductId(requestDto.getProductIds());

        /// Order 생성
        Order order = Order.createOrder(userId);

        // OrderProduct 생성 및 연관 설정
        List<OrderProduct> orderProducts = requestDto.getProductIds().stream()
                .map(orderProductRequestDto -> orderProductRequestDto.toEntity(order, orderProductRequestDto.getProductId()))
                .toList();

        order.setProductIds(orderProducts);

        // Order 저장 (Cascade 설정으로 OrderProduct도 함께 저장됨)
        orderRepository.save(order);

        return OrderResponseDto.from(order);
    }

    private void checkProductId(List<OrderProductRequestDto> orderProducts) {
        // 주문 상품 리스트가 비어 있거나 null 인 경우
        if (orderProducts == null || orderProducts.isEmpty()) {
            throw new IllegalArgumentException("주문할 상품이 없습니다.");
        }

        // 상품 목록을 조회
        List<ProductResponseDto> products = productClient.getProducts();

        // 조회된 상품 목록이 비어있거나 null인 경우
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("주문 가능한 상품이 없습니다.");
        }

        // 주문 상품 리스트에서 각 상품 ID가 존재하는지 확인
        for (OrderProductRequestDto orderProduct : orderProducts) {
            boolean validProduct = products.stream()
                    .anyMatch(product -> product.getProductId().equals(orderProduct.getProductId()));
                            //&& product.getQuantity() >= 1); // ProductId 비교

            if (!validProduct) {
                throw new IllegalArgumentException("존재하지 않거나 수량이 부족한 상품입니다. 상품 ID: " + orderProduct.getProductId());
            }
        }

    }

    public OrderResponseDto  fallbackCreateOrder(Throwable t) {
        log.info("fallback 발생 원인 : {}", t.getMessage());
        throw new IllegalArgumentException("잠시 후에 주문 추가를 요청해 주세요.");
    }

    @Transactional
    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackCreateOrder")
    public OrderResponseDto updateOrder(Long orderId, OrderRequestDto requestDto, String userId) {
        Order order = orderRepository.findById(orderId)
                .filter(o -> o.getDeletedAt() == null)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found or has been deleted"));

        checkProductId(requestDto.getProductIds());

        for (OrderProductRequestDto dto : requestDto.getProductIds()) {
            OrderProduct newProduct = dto.toEntity(order, dto.getProductId());
            order.getProductIds().add(newProduct);
        }

        order.updateOrder(order.getProductIds(), userId, OrderStatus.valueOf(String.valueOf(requestDto.getStatus())));
        Order updatedOrder = orderRepository.save(order);

        return OrderResponseDto.from(updatedOrder);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "orderCache", key = "args[0]")
    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .filter(o -> o.getDeletedAt() == null)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found or has been deleted"));
        return OrderResponseDto.from(order);
    }

/*
    public Page<OrderResponseDto> getOrders(OrderSearchDto searchDto, Pageable pageable, String role, String userId) {
        return orderRepository.searchOrders(searchDto, pageable,role, userId);
    }

    @Transactional
    public void deleteOrder(Long orderId, String deletedBy) {
        Order order = orderRepository.findById(orderId)
                .filter(o -> o.getDeletedAt() == null)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found or has been deleted"));
        order.deleteOrder(deletedBy);
        orderRepository.save(order);
    }

    private OrderResponseDto toResponseDto(Order order) {
        return new OrderResponseDto(
                order.getOrderId(),
                order.getStatus().name(),
                order.getCreatedAt(),
                order.getCreatedBy(),
                order.getUpdatedAt(),
                order.getUpdatedBy(),
                order.getOrderItemIds()
        );
    }
*/

}