package com.sparta.msa_exam.product.controller;

import com.sparta.msa_exam.product.dto.ProductSearchDto;
import com.sparta.msa_exam.product.dto.request.ProductRequestDto;
import com.sparta.msa_exam.product.dto.response.ProductResponseDto;
import com.sparta.msa_exam.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto productRequestDto,
                                                                              @RequestHeader(value = "X-User-Id", required = true) String userId,
                                                                              @RequestHeader(value = "X-Role", required = true) String role) {
        if (!"ADMIN".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. User role is not MANAGER.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productRequestDto, userId));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getProducts(ProductSearchDto searchDto,
                                                                @RequestParam(required = false) String sortField,
                                                                @RequestParam(required = false) boolean ascending) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProducts(searchDto, sortField, ascending));
    }
    /*
    @GetMapping("/{productId}")
    public ProductResponseDto getProductById(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    @PutMapping("/{productId}")
    public ProductResponseDto updateProduct(@PathVariable Long productId,
                                            @RequestBody ProductRequestDto orderRequestDto,
                                            @RequestHeader(value = "X-User-Id", required = true) String userId,
                                            @RequestHeader(value = "X-Role", required = true) String role) {
        return productService.updateProduct(productId, orderRequestDto, userId);
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable Long productId, @RequestParam String deletedBy) {
        productService.deleteProduct(productId, deletedBy);
    }

    @GetMapping("/{id}/reduceQuantity")
    public void reduceProductQuantity(@PathVariable Long id, @RequestParam int quantity) {
        productService.reduceProductQuantity(id, quantity);
    }
    */

}
