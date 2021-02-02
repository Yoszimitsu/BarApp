package com.bar.orderitems.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderItemRequest {

    @NotNull(message = "Product ID must not be null.")
    private Long productId;
    @NotNull(message = "Quantity must not be null.")
    private Integer quantity;
}
