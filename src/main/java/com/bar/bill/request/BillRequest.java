package com.bar.bill.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Builder
public class BillRequest {

    @NotNull(message = "Order ID must not be empty.")
    private Long orderId;
    @NotNull(message = "Company name must not be empty.")
    private String company;
    @NotNull(message = "Data and Time must not be empty.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;
    private String nipNumber;
    private String customerName;
}
