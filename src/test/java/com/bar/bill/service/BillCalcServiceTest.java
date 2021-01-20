package com.bar.bill.service;

import com.bar.order.dto.OrderDto;
import com.bar.order.service.OrderService;
import com.bar.order.service.mapper.OrderMapperService;
import com.bar.orderitems.dto.OrderItemDto;
import com.bar.product.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BillCalcServiceTest {

    @InjectMocks
    BillCalcService billCalcService;

    @Mock
    OrderService orderService;
    @Mock
    OrderMapperService orderMapperService;

    OrderDto orderDto;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);

        Product productOne = Product.builder()
                .id(1L)
                .name("productOne")
                .priceNet(1.00)
                .build();

        Product productTwo = Product.builder()
                .id(2L)
                .name("productTwo")
                .priceNet(3.00)
                .build();

        OrderItemDto orderItemDtoOne = OrderItemDto.builder()
                .id(1L)
                .product(productOne)
                .quantity(1)
                .build();

        OrderItemDto orderItemDtoTwo = OrderItemDto.builder()
                .id(2L)
                .product(productTwo)
                .quantity(3)
                .build();

        orderDto = OrderDto.builder()
                .id(1L)
                .orderItemList(new ArrayList<>() {{
                    add(orderItemDtoOne);
                    add(orderItemDtoTwo);
                }})
                .employeeId(1)
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .build();
    }

    @Test
    void priceNetCalc() {
        when(orderMapperService.mapToDto(any())).thenReturn(orderDto);

        double result = billCalcService.priceNetCalc(1);

        assertEquals(10.00, result);
    }
}
