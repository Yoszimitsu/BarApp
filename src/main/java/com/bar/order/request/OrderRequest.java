package com.bar.order.request;

import com.bar.orderitems.request.OrderItemRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderRequest {

    @NotNull(message = "Order list must not be empty.")
    private List<OrderItemRequest> orderItemList;
    @NotNull(message = "Employee ID must not be empty.")
    private int employeeId;
    @NotNull(message = "Date&Time must not be empty.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;
}
