package com.bar.bill.service;

import com.bar.order.service.OrderService;
import com.bar.order.service.mapper.OrderMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillCalcService {

    @Autowired
    OrderService orderService;
    @Autowired
    OrderMapperService orderMapperService;


    /**
     * priceNetCals() is simple method to calculate a Bill value.
     * The method return Double, what may generate a round mistakes!
     * <p>
     * To use this Api in real case, change Double.class to BigDecimal.class!
     *
     * @return the price of entire Order
     */
    public Double priceNetCalc(long orderId) {
        var orderDto = orderMapperService.mapToDto(orderService.get(orderId));

        return round(
                orderDto.getOrderItemList()
                        .stream()
                        .mapToDouble(orderItemDto ->
                                orderItemDto.getProduct().getPriceNet() * orderItemDto.getQuantity())
                        .sum(),
                2);
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
