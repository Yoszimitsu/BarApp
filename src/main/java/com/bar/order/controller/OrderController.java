package com.bar.order.controller;

import com.bar.order.dto.OrderDto;
import com.bar.order.mapper.OrderMapper;
import com.bar.order.request.OrderRequest;
import com.bar.order.service.OrderService;
import com.bar.system.endpoint.Endpoint;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller()
@RequestMapping(OrderController.ORDER_ENDPOINT)
@AllArgsConstructor
@Slf4j
public class OrderController {

    protected static final String ORDER_ENDPOINT = Endpoint.API_ROOT + Endpoint.URN_ORDER;
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<OrderDto> get(@PathVariable long id) {
        log.info("GET " + ORDER_ENDPOINT + "/" + id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderMapper.mapToDto(orderService.get(id)));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<OrderDto>> getAll() {
        log.info("GET " + ORDER_ENDPOINT);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderMapper.mapToOrderDtoList(orderService.getAll()));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<OrderDto> add(@Valid @RequestBody OrderRequest orderRequest) {
        log.info("POST " + ORDER_ENDPOINT);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderMapper.mapToDto(orderService.add(orderRequest)));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<OrderDto> update(@PathVariable long id, @Valid @RequestBody OrderRequest orderRequest) {
        log.info("PUT " + ORDER_ENDPOINT + "/" + id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderMapper.mapToDto(orderService.update(id, orderRequest)));
    }

    @DeleteMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> delete(@PathVariable long id) {
        log.info("DELETE " + ORDER_ENDPOINT + "/" + id);
        orderService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
