package com.bar.orderitems.service;

import com.bar.order.entity.Order;
import com.bar.orderitems.entity.OrderItem;
import com.bar.orderitems.mapper.OrderItemMapper;
import com.bar.orderitems.repository.OrderItemRepository;
import com.bar.orderitems.request.OrderItemRequest;
import com.bar.product.entity.Product;
import com.bar.product.repository.ProductRepository;
import com.bar.product.service.ProductService;
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

class OrderItemServiceTest {

    @InjectMocks
    OrderItemService orderItemService;
    @Mock
    OrderItemMapper orderItemMapper;
    @Mock
    OrderItemRepository orderItemRepository;
    @Mock
    ProductService productService;
    @Mock
    ProductRepository productRepository;

    Order order;
    Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        order = Order.builder()
                .id(1L)
                .employeeId(1)
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .build();

        product = Product.builder()
                .id(1L)
                .name("test")
                .priceNet(1.23)
                .build();
    }

    @Test
    void getOrderItem() {
        OrderItem orderItem = OrderItem.builder()
                .id(1L)
                .order(order)
                .product(product)
                .quantity(10)
                .build();

        when(orderItemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(orderItem));

        OrderItem result = orderItemService.get(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(order, result.getOrder());
        assertEquals(product, result.getProduct());
        assertEquals(10, result.getQuantity());
    }

    @Test
    void getOrderItem_NotFoundException() {
        when(orderItemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        assertThrows(NotFoundException.class,
                () -> {
                    orderItemService.get(1);
                }
        );
    }

    @Test
    void getList() {
        long id = 1;
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem1 = OrderItem.builder()
                .id(1L)
                .order(order)
                .product(product)
                .quantity(10)
                .build();

        OrderItem orderItem2 = OrderItem.builder()
                .id(1L)
                .order(order)
                .product(product)
                .quantity(20)
                .build();
        orderItemList.add(orderItem1);
        orderItemList.add(orderItem2);

        when(orderItemRepository.findOrderItemByOrderId(anyLong())).thenReturn(orderItemList);

        List<OrderItem> result = orderItemService.getList(id);

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(order, result.get(0).getOrder());
        assertEquals(product, result.get(0).getProduct());
        assertEquals(10, result.get(0).getQuantity());

        assertEquals(order, result.get(1).getOrder());
        assertEquals(product, result.get(1).getProduct());
        assertEquals(20, result.get(1).getQuantity());
    }

    @Test
    void getList_NotFoundException() {
        long id = 1;

        when(orderItemRepository.findOrderItemByOrderId(anyLong())).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> {
                    orderItemService.getList(id);
                });
    }

    @Test
    void getAllOrderItems() {
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem1 = OrderItem.builder()
                .id(1L)
                .order(order)
                .product(product)
                .quantity(10)
                .build();

        OrderItem orderItem2 = OrderItem.builder()
                .id(2L)
                .order(order)
                .product(product)
                .quantity(20)
                .build();
        orderItemList.add(orderItem1);
        orderItemList.add(orderItem2);

        when(orderItemRepository.findAll()).thenReturn(orderItemList);

        List<OrderItem> result = orderItemService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(order, result.get(0).getOrder());
        assertEquals(product, result.get(0).getProduct());
        assertEquals(10, result.get(0).getQuantity());

        assertEquals(order, result.get(1).getOrder());
        assertEquals(product, result.get(1).getProduct());
        assertEquals(20, result.get(1).getQuantity());
    }

    @Test
    void addOrderItem() {
        OrderItem orderItem = OrderItem.builder()
                .id(1L)
                .order(order)
                .product(product)
                .quantity(10)
                .build();
        OrderItemRequest orderItemRequest = OrderItemRequest.builder()
                .productId(1L)
                .quantity(10)
                .build();

        when(productService.get(anyLong())).thenReturn(product);
        when(orderItemRepository.save(any())).thenReturn(orderItem);

        OrderItem result = orderItemService.add(orderItemRequest, order);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(order, result.getOrder());
        assertEquals(product, result.getProduct());
        assertEquals(10, result.getQuantity());
    }

    @Test
    void addOrderItem_NotFoundExceptionForProduct() {
        OrderItemRequest orderItemRequest = OrderItemRequest.builder()
                .productId(1L)
                .quantity(10)
                .build();

        when(productService.get(anyLong())).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class,
                () -> {
                    orderItemService.add(orderItemRequest, order);
                });
    }

    @Test
    void addOrderItemList() {
        List<OrderItemRequest> orderItemRequestList = new ArrayList<>();
        OrderItem orderItem1 = OrderItem.builder()
                .id(1L)
                .order(order)
                .product(product)
                .quantity(10)
                .build();

        OrderItemRequest orderItemRequest = OrderItemRequest.builder()
                .productId(1L)
                .quantity(10)
                .build();

        orderItemRequestList.add(orderItemRequest);
        orderItemRequestList.add(orderItemRequest);

        when(orderItemRepository.save(any())).thenReturn(orderItem1);

        List<OrderItem> result = orderItemService.add(orderItemRequestList, order);

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(1, result.get(0).getId());
        assertEquals(order, result.get(0).getOrder());
        assertEquals(product, result.get(0).getProduct());
        assertEquals(10, result.get(0).getQuantity());

        assertEquals(1, result.get(1).getId());
        assertEquals(order, result.get(1).getOrder());
        assertEquals(product, result.get(1).getProduct());
        assertEquals(10, result.get(1).getQuantity());
    }

    @Test
    void updateOrderItem() {
        long id = 1;
        OrderItem orderItem = OrderItem.builder()
                .id(1L)
                .order(order)
                .product(product)
                .quantity(10)
                .build();

        OrderItemRequest orderItemRequest = OrderItemRequest.builder()
                .productId(1L)
                .quantity(10)
                .build();

        when(orderItemRepository.save(any())).thenReturn(orderItem);
        when(orderItemRepository.existsById(anyLong())).thenReturn(true);

        OrderItem result = orderItemService.update(id, orderItemRequest);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(order, result.getOrder());
        assertEquals(product, result.getProduct());
        assertEquals(10, result.getQuantity());
    }

    @Test
    void updateOrderItem_NotFoundException() {
        long id = 1;
        OrderItemRequest orderItemRequest = OrderItemRequest.builder()
                .productId(1L)
                .quantity(10)
                .build();

        when(orderItemRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> {
                    orderItemService.update(id, orderItemRequest);
                });
    }

    @Test
    void deleteOrderItem() {
        long id = 1;
        when(orderItemRepository.existsById(anyLong())).thenReturn(true);

        orderItemService.delete(1);

        verify(orderItemRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteAllByOrderId() {
        long id = 1;
        when(orderItemRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> {
                    orderItemService.delete(id);
                });
    }
}
