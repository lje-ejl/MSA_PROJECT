package com.sparta.msa_exam.product.dto.response;


import com.sparta.msa_exam.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto implements Serializable {

    private static final long serialVersionUID = 1L; // 버전 관리 (옵션)

    private Long productId;
    private String name;
    private Integer supplyPrice;


    public static ProductResponseDto from(Product product) {
        return ProductResponseDto.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .supplyPrice(product.getSupplyPrice())
                .build();
    }
}