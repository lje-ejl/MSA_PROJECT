package com.sparta.msa_exam.order.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.bytecode.enhance.spi.EnhancementContext;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "p_order_product")  // 테이블 이름 설정
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id", updatable = false, nullable = false)
    private Long orderProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "orderId", nullable = false)  // 외래 키 설정 (user_id)
    private Order order; // 주문 ID

    private Long productId; // 상품 ID

}