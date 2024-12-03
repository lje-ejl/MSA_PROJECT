package com.sparta.msa_exam.order.dto.request;

import com.sparta.msa_exam.order.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private Long orderId;
    private OrderStatus status;
    private List<OrderProductRequestDto> productIds;
}
