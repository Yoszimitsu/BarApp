package com.bar.orderitems.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderItemRequest {

    @NotNull(message = "ProductID must not be null.")
    private long productId;
    @NotNull(message = "Quantity must not be null.")
    private int quantity;
}
