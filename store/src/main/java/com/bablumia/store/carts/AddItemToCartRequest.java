package com.bablumia.store.carts;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemToCartRequest {
    @NotNull(message = "Product ID is required")
    @Min(value = 1, message = "Product ID must be a positive number")
    private Long productId;
}
