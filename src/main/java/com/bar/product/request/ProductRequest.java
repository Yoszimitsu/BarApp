package com.bar.product.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProductRequest {

    @NotNull(message = "Product name must not be empty.")
    private String name;

    @PositiveOrZero
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    @NotNull(message = "Price must not be empty.")
    private Double priceNet;
}
