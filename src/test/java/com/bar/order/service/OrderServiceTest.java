package com.bar.order.service;

import com.bar.order.entity.Order;
import com.bar.order.repository.OrderRepository;
import com.bar.order.request.OrderRequest;
import com.bar.orderitems.request.OrderItemRequest;
import com.bar.orderitems.service.OrderItemService;
import com.bar.product.entity.Product;
import com.bar.system.error.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderItemService orderItemService;

    OrderItemRequest orderItemRequest;
    Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        product = Product.builder()
                .id(1L)
                .name("test")
                .priceNet(1.23)
                .build();

        orderItemRequest = OrderItemRequest.builder()
                .productId(1L)
                .quantity(10)
                .build();
    }

    @Test
    void getOrder() {
        long id = 1;
        Order order = Order.builder()
                .id(1L)
                .employeeId(1)
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .build();

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));

        Order result = orderService.get(id);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1, result.getEmployeeId());
        assertEquals(LocalDateTime.of(2021, 1, 1, 12, 30, 15), result.getDateTime());
    }

    @Test
    void getOrder_NotFoundException() {
        long id = 1;
        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));

        assertThrows(NotFoundException.class,
                () -> {
                    orderService.get(id);
                });
    }

    @Test
    void getAllOrders() {
        List<Order> orderList = new ArrayList<>();
        Order order1 = Order.builder()
                .id(1L)
                .employeeId(1)
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .build();
        Order order2 = Order.builder()
                .id(2L)
                .employeeId(2)
                .dateTime(LocalDateTime.of(2021, 1, 2, 12, 30, 15))
                .build();
        orderList.add(order1);
        orderList.add(order2);

        when(orderRepository.findAll()).thenReturn(orderList);

        List<Order> result = orderService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(1, result.get(0).getId());
        assertEquals(1, result.get(0).getEmployeeId());
        assertEquals(LocalDateTime.of(2021, 1, 1, 12, 30, 15), result.get(0).getDateTime());

        assertEquals(2, result.get(1).getId());
        assertEquals(2, result.get(1).getEmployeeId());
        assertEquals(LocalDateTime.of(2021, 1, 2, 12, 30, 15), result.get(1).getDateTime());
    }

    @Test
    void addOrder() {
        List<OrderItemRequest> orderItemList = new ArrayList<>();
        orderItemList.add(orderItemRequest);

        Order order = Order.builder()
                .id(1L)
                .employeeId(1)
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .build();
        OrderRequest orderRequest = OrderRequest.builder()
                .orderItemList(orderItemList)
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .employeeId(1)
                .build();

        when(orderRepository.save(any())).thenReturn(order);

        Order result = orderService.add(orderRequest);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1, result.getEmployeeId());
        assertEquals(LocalDateTime.of(2021, 1, 1, 12, 30, 15), result.getDateTime());
    }

    @Test
    void updateOrder() {
        List<OrderItemRequest> orderItemList = new ArrayList<>();
        orderItemList.add(orderItemRequest);
        long id = 1;
        Order order = Order.builder()
                .id(1L)
                .employeeId(1)
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .build();
        OrderRequest orderRequest = OrderRequest.builder()
                .orderItemList(orderItemList)
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .employeeId(1)
                .build();

        when(orderRepository.save(any())).thenReturn(order);
        when(orderRepository.existsById(anyLong())).thenReturn(true);

        Order result = orderService.update(id, orderRequest);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1, result.getEmployeeId());
        assertEquals(LocalDateTime.of(2021, 1, 1, 12, 30, 15), result.getDateTime());
    }

    @Test
    void updateOrder_ProductNotFound() {
        long id = 1;
        List<OrderItemRequest> orderItemList = new ArrayList<>();
        orderItemList.add(orderItemRequest);

        OrderRequest orderRequest = OrderRequest.builder()
                .orderItemList(orderItemList)
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .employeeId(1)
                .build();

        when(orderRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> {
                    orderService.update(id, orderRequest);
                }
        );
    }

    @Test
    void deleteOrder() {
        long id = 1;
        when(orderRepository.existsById(anyLong())).thenReturn(true);

        orderService.delete(1);

        verify(orderRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteOrder_NotFoundException() {
        long id = 1;
        when(orderRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> {
                    orderService.delete(id);
                });
    }
}
