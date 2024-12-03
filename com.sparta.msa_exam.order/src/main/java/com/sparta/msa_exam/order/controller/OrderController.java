package com.sparta.msa_exam.order.controller;

import com.sparta.msa_exam.order.dto.OrderSearchDto;
import com.sparta.msa_exam.order.dto.request.OrderRequestDto;
import com.sparta.msa_exam.order.dto.response.OrderResponseDto;
import com.sparta.msa_exam.order.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto requestDto,
                                                        @RequestHeader(value = "X-User-Id", required = true) String userId) {
        OrderResponseDto responseDto = orderService.createOrder(requestDto, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable Long orderId,
                                        @RequestBody OrderRequestDto orderRequestDto,
                                        @RequestHeader(value = "X-User-Id", required = true) String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrder(orderId, orderRequestDto, userId));
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderById(orderId));
    }


/*
    @GetMapping
    public Page<OrderResponseDto> getOrders(OrderSearchDto searchDto, Pageable pageable,
                                            @RequestHeader(value = "X-User-Id", required = true) String userId,
                                            @RequestHeader(value = "X-Role", required = true) String role) {
        // 역할이 MANAGER인지 확인
        if (!"ADMIN".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. User role is not MANAGER.");
        }
        return orderService.getOrders(searchDto, pageable,role, userId);
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable Long orderId, @RequestParam String deletedBy) {
        orderService.deleteOrder(orderId, deletedBy);
    }

 */
}
