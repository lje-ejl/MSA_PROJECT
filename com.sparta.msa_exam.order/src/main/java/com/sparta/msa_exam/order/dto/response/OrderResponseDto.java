package com.sparta.msa_exam.order.dto.response;

import com.sparta.msa_exam.order.domain.Order;
import com.sparta.msa_exam.order.domain.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto implements Serializable {

    private static final long serialVersionUID = 1L; // 버전 관리 (옵션)

    private Long orderId;
    private String status;
    private List<OrderProductResponseDto> productIds;

    public static OrderResponseDto from(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .status(order.getStatus().name())
                .productIds(order.getProductIds().stream()
                        .map(OrderProductResponseDto::from)
                        .toList())
                .build();
    }


}
