package com.bar.bill.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BillRequest {

    @NotNull(message = "Order ID must not be empty.")
    private long orderId;
    @NotNull(message = "Company name must not be empty.")
    private String company;
    @NotNull(message = "Data and Time must not be empty.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;
    @NotNull(message = "Price net must not be empty.")
    private double priceNet;
    private String nipNumber;
    private String customerName;
}
