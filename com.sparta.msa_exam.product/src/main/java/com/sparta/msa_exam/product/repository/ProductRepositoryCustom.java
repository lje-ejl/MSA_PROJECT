package com.sparta.msa_exam.product.repository;

import com.sparta.msa_exam.product.dto.ProductSearchDto;
import com.sparta.msa_exam.product.dto.response.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {
    List<ProductResponseDto> searchProducts(ProductSearchDto searchDto, String sortField, boolean ascending);
}
