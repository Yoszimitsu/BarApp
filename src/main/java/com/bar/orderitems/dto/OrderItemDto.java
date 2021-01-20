package com.bar.orderitems.dto;

import com.bar.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderItemDto {

    private long id;
    private Product product;
    private int quantity;
}
