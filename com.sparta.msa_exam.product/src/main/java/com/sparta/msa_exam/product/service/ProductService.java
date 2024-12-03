package com.sparta.msa_exam.product.service;


import com.sparta.msa_exam.product.domain.Product;
import com.sparta.msa_exam.product.dto.ProductSearchDto;
import com.sparta.msa_exam.product.dto.request.ProductRequestDto;
import com.sparta.msa_exam.product.dto.response.ProductResponseDto;
import com.sparta.msa_exam.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    @CachePut(cacheNames = "productCache", key = "#result.productId")
    @CacheEvict(cacheNames = "productAllCache", allEntries = true)
    public ProductResponseDto createProduct(ProductRequestDto requestDto, String userId) {
        Product product = Product.createProduct(requestDto, userId);
        Product savedProduct = productRepository.save(product);
        return ProductResponseDto.from(savedProduct);
    }

    @Cacheable(cacheNames = "productAllCache", key = "methodName")
    public List<ProductResponseDto> getProducts(ProductSearchDto searchDto, String sortField, boolean ascending) {
        return productRepository.searchProducts(searchDto, sortField, ascending);
    }

    /*
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .filter(p -> p.getDeletedAt() == null)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found or has been deleted"));
        return ProductResponseDto.from(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long productId, ProductRequestDto requestDto, String userId) {
        Product product = productRepository.findById(productId)
                .filter(p -> p.getDeletedAt() == null)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found or has been deleted"));

        product.updateProduct(requestDto.getName(), requestDto.getDescription(), requestDto.getSupplyPrice(), requestDto.getQuantity(), userId);
        Product updatedProduct = productRepository.save(product);

        return ProductResponseDto.from(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long productId, String deletedBy) {
        Product product = productRepository.findById(productId)
                .filter(p -> p.getDeletedAt() == null)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found or has been deleted"));
        product.deleteProduct(deletedBy);
        productRepository.save(product);
    }

    @Transactional
    public void reduceProductQuantity(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough quantity for product ID: " + productId);
        }

        product.reduceQuantity(quantity);
        productRepository.save(product);
    }
    */

}