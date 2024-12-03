package com.sparta.msa_exam.order.dto.response;

import com.sparta.msa_exam.order.domain.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductResponseDto implements Serializable {

    private static final long serialVersionUID = 1L; // 버전 관리 (옵션)

    private Long orderProductId;
    private Long productId;

    public static OrderProductResponseDto from(OrderProduct orderProduct) {
        return OrderProductResponseDto.builder()
                .productId(orderProduct.getProductId())
                .orderProductId(orderProduct.getOrderProductId())
                .build();
    }
}
