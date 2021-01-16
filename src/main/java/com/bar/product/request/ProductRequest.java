package com.bar.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProductRequest {

    @NotBlank(message = "Product name must not be empty")
    private String name;

    @NotBlank(message = "Price must not be empty")
    @PositiveOrZero
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private double price_net;
}
