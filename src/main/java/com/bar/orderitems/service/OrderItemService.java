package com.bar.orderitems.service;

import com.bar.order.entity.Order;
import com.bar.orderitems.entity.OrderItem;
import com.bar.orderitems.repository.OrderItemRepository;
import com.bar.orderitems.request.OrderItemRequest;
import com.bar.product.service.ProductService;
import com.bar.system.error.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {

    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    ProductService productService;

    public OrderItem get(long id) {
        return Optional.ofNullable(orderItemRepository.findById(id)).get()
                .orElseThrow(() -> new NotFoundException("OrderItem not found ", Long.toString(id)));
    }

    public List<OrderItem> getList(long orderId) {
        return Optional.ofNullable(orderItemRepository.findOrderItemByOrderId(orderId))
                .orElseThrow(() -> new NotFoundException(
                        "Not found OrderItems with the specific OrderId value",
                        Long.toString(orderId)));
    }

    public List<OrderItem> getAll() {
        return orderItemRepository.findAll();
    }

    public OrderItem add(OrderItemRequest orderItemRequest, Order order) {
        var orderItem = OrderItem.builder()
                .order(order)
                .product(productService.get(orderItemRequest.getProductId()))
                .quantity(orderItemRequest.getQuantity())
                .build();
        return orderItemRepository.save(orderItem);
    }

    public List<OrderItem> add(List<OrderItemRequest> orderItemRequestList, Order order) {
        List<OrderItem> orderItemList = new ArrayList<>();
        for (OrderItemRequest orderItemRequest : orderItemRequestList) {
            orderItemList.add(add(orderItemRequest, order));
        }
        return orderItemList;
    }

    public OrderItem update(long id, OrderItemRequest orderItemRequest) {
        checkIfOrderItemExistsInDatabase(id);
        var orderItem = OrderItem.builder()
                .id(id)
                .product(productService.get(orderItemRequest.getProductId()))
                .quantity(orderItemRequest.getQuantity())
                .build();
        return orderItemRepository.save(orderItem);
    }

    public void delete(long id) {
        checkIfOrderItemExistsInDatabase(id);
        orderItemRepository.deleteById(id);
    }

    public void deleteAllByOrderId(long id) {
        var orderItemList = getList(id);
        orderItemList.forEach(orderItem -> delete(orderItem.getId()));
    }

    private void checkIfOrderItemExistsInDatabase(long id) {
        if (!orderItemRepository.existsById(id)) {
            throw new NotFoundException("OrderItem not found", Long.toString(id));
        }
    }
}
