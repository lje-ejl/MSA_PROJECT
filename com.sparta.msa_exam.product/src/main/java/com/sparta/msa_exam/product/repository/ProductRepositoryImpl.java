package com.sparta.msa_exam.product.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.msa_exam.product.domain.Product;
import com.sparta.msa_exam.product.dto.ProductSearchDto;
import com.sparta.msa_exam.product.dto.response.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.msa_exam.product.domain.QProduct.product;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProductResponseDto> searchProducts(ProductSearchDto searchDto, String sortField, boolean ascending) {
        List<OrderSpecifier<?>> orders = getAllOrderSpecifiers(sortField, ascending);

        QueryResults<Product> results = queryFactory
                .selectFrom(product)
                .where(
                        nameContains(searchDto.getName()),
                        priceBetween(searchDto.getMinPrice(), searchDto.getMaxPrice())
                )
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .fetchResults();

        List<ProductResponseDto> content = results.getResults().stream()
                .map(Product::toResponseDto)
                .collect(Collectors.toList());


        return content;
    }

    private BooleanExpression nameContains(String name) {
        return name != null ? product.name.containsIgnoreCase(name) : null;
    }



    private BooleanExpression priceBetween(Double minPrice, Double maxPrice) {
        if (minPrice != null && maxPrice != null) {
            return product.supplyPrice.between(minPrice, maxPrice);
        } else if (minPrice != null) {
            return product.supplyPrice.goe(minPrice);
        } else if (maxPrice != null) {
            return product.supplyPrice.loe(maxPrice);
        } else {
            return null;
        }
    }



    private List<OrderSpecifier<?>> getAllOrderSpecifiers(String sortField, boolean ascending) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (sortField != null) {
            com.querydsl.core.types.Order direction = ascending ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC;

            // sortField를 통해 어떤 필드를 기준으로 정렬할 것인지 설정
            switch (sortField) {
                case "createdAt":
                    orders.add(new OrderSpecifier<>(direction, product.createdAt));
                    break;
                case "supplyPrice":
                    orders.add(new OrderSpecifier<>(direction, product.supplyPrice));
                    break;
                case "quantity":
                    orders.add(new OrderSpecifier<>(direction, product.quantity));
                    break;
                case "name":
                    orders.add(new OrderSpecifier<>(direction, product.name));
                    break;
                default:
                    // 기본적으로 createdAt 기준으로 정렬
                    orders.add(new OrderSpecifier<>(direction, product.createdAt));
                    break;
            }
        }

        return orders;
    }
}