package com.bar.order.service;

import com.bar.order.entity.Order;
import com.bar.order.repository.OrderRepository;
import com.bar.order.request.OrderRequest;
import com.bar.orderitems.service.OrderItemService;
import com.bar.system.error.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderItemService orderItemService;

    public Order get(long id) {
        return Optional.of(orderRepository.findById(id)).get()
                .orElseThrow(() -> new NotFoundException("Order not found", Long.toString(id)));
    }

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order add(OrderRequest orderRequest) {
        var order = Order.builder()
                .employeeId(orderRequest.getEmployeeId())
                .dateTime(orderRequest.getDateTime())
                .build();

        order = orderRepository.save(order);
        orderItemService.add(orderRequest.getOrderItemList(), order);
        return order;
    }

    public Order update(long id, OrderRequest orderRequest) {
        checkIfOrderExistsInDatabase(id);
        var order = Order.builder()
                .id(id)
                .employeeId(orderRequest.getEmployeeId())
                .dateTime(orderRequest.getDateTime())
                .build();
        return orderRepository.save(order);
    }

    public void delete(long id) {
        checkIfOrderExistsInDatabase(id);
        orderItemService.deleteAllByOrderId(id);
        orderRepository.deleteById(id);
    }

    private void checkIfOrderExistsInDatabase(long productId) {
        if (!orderRepository.existsById(productId)) {
            throw new NotFoundException("Order not found", Long.toString(productId));
        }
    }
}
