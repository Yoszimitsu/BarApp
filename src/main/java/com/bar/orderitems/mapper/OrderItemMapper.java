package com.bar.orderitems.mapper;

import com.bar.orderitems.dto.OrderItemDto;
import com.bar.orderitems.entity.OrderItem;
import com.bar.orderitems.service.OrderItemService;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItem mapToEntity(OrderItemDto orderItemDto);

    OrderItemDto mapToDto(OrderItem orderItem, OrderItemService orderItemService, OrderItemMapper orderItemMapper);

    List<OrderItemDto> mapToDtoList(List<OrderItem> orderItemList);
}
