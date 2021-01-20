package com.bar.order.service.mapper;

import com.bar.order.dto.OrderDto;
import com.bar.order.entity.Order;
import com.bar.orderitems.mapper.OrderItemMapper;
import com.bar.orderitems.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderMapperService {

    @Autowired
    OrderItemService orderItemService;
    @Autowired
    OrderItemMapper orderItemMapper;

    public OrderDto mapToDto(Order order) {
        var orderDto = OrderDto.builder()
                .id(order.getId())
                .employeeId(order.getEmployeeId())
                .dateTime(order.getDateTime())
                .orderItemList(orderItemMapper.mapToDtoList(orderItemService.getList(order.getId())))
                .build();
        return orderDto;
    }

    /**
     * Default mapStruct.Mapper library implementation.
     * Method maps OrderDto to Order object.
     */
    public Order mapToEntity(OrderDto orderDto) {
        if (orderDto == null) {
            return null;
        }

        Order order = new Order();

        order.setId(orderDto.getId());
        order.setEmployeeId(orderDto.getEmployeeId());
        order.setDateTime(orderDto.getDateTime());
        return order;
    }

    /**
     * Default mapStruct.Mapper library implementation.
     * Method maps List of Order to List of OrderDto collection.
     */
    public List<OrderDto> mapToOrderDtoList(List<Order> orderList) {
        if (orderList == null) {
            return null;
        }

        List<OrderDto> list = new ArrayList<OrderDto>(orderList.size());
        for (Order order : orderList) {
            list.add(mapToDto(order));
        }
        return list;
    }
}
