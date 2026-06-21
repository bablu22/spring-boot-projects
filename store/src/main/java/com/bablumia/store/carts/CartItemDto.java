package com.bablumia.store.carts;

import java.math.BigDecimal;

import com.bablumia.store.products.ProductDto;

import lombok.Data;

@Data
public class CartItemDto {
    private Long id;
    private ProductDto product;
    private Integer quantity;
    private BigDecimal totalPrice;
}
