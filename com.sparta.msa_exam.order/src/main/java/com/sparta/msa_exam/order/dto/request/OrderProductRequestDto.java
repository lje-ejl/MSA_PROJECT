package com.sparta.msa_exam.order.dto.request;

import com.sparta.msa_exam.order.domain.Order;
import com.sparta.msa_exam.order.domain.OrderProduct;
import com.sparta.msa_exam.order.dto.response.OrderResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductRequestDto {
    private Long orderId;
    private Long productId;

    // OrderProductRequestDto를 OrderProduct 엔티티로 변환하는 메소드
    public OrderProduct toEntity(Order order, Long productId) {
        return OrderProduct.builder()
                .order(order)   // 전달된 Order 객체로 설정
                .productId(productId) // 전달된 Product 객체로 설정
                .build();
    }


}
