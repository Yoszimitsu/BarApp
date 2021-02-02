package com.bar.order.controller;

import com.bar.order.request.OrderRequest;
import com.bar.order.service.OrderService;
import com.bar.order.service.mapper.OrderMapperService;
import com.bar.orderitems.request.OrderItemRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderControllerTest {

    @InjectMocks
    OrderController orderController;

    @Mock
    OrderService orderService;
    @Mock
    OrderMapperService orderMapperService;

    OrderItemRequest orderItemRequest;

    @BeforeAll
    static void init() {
        Map<String, Object> newProduct = new HashMap<>();
        newProduct.put("name", "test1");
        newProduct.put("priceNet", 2.00);


        given().
            contentType("application/json").
            accept("application/json").
            body(newProduct).
        when().
            post("/api/product").
        then().
            statusCode(201).
            contentType("application/json").
            extract().
            response();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;

        orderItemRequest = OrderItemRequest.builder()
                .productId(1L)
                .quantity(10)
                .build();
    }

    @Test()
    @Order(1)
    void addOrder_StatusCode_201() {
        List<OrderItemRequest> orderItemListReq = new ArrayList<>();
        orderItemListReq.add(orderItemRequest);

        OrderRequest orderRequest = OrderRequest.builder()
                .orderItemList(orderItemListReq)
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .employeeId(1)
                .build();

        Response response =
                given().
                    contentType("application/json").
                    accept("application/json").
                    body(orderRequest).
                when().
                    post("/api/order").
                then().
                    statusCode(201).
                    contentType("application/json").
                    extract().
                    response();

        String id = response.jsonPath().getString("id");
        Integer employeeId = Integer.parseInt(response.jsonPath().getString("employeeId"));
        String dateTime = response.jsonPath().getString("dateTime");

        assertNotNull(id);
        assertEquals(1, employeeId);
        assertEquals(LocalDateTime.of(2021, 1, 1, 12, 30, 15).toString(), dateTime);
    }

    @Test()
    void addProduct_ArgumentNotValidException_400() {
        List<OrderItemRequest> orderItemListReq = new ArrayList<>();
        orderItemListReq.add(orderItemRequest);

        OrderRequest orderRequest = OrderRequest.builder()
                .orderItemList(orderItemListReq)
                .dateTime(null)
                .employeeId(1)
                .build();

        given().
            contentType("application/json").
            accept("application/json").
            body(orderRequest).
        when().
            post("/api/order").
        then().
            statusCode(400).
            contentType("application/json").
            extract().
            response();
    }

    @Test()
    void addProduct_notEnoughArgument_400() {
        List<OrderItemRequest> orderItemListReq = new ArrayList<>();
        orderItemListReq.add(orderItemRequest);

        OrderRequest orderRequest = OrderRequest.builder()
                .orderItemList(orderItemListReq)
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .build();

        given().
            contentType("application/json").
            accept("application/json").
            body(orderRequest).
        when().
            post("/api/order").
        then().
            statusCode(400).
            contentType("application/json").
            extract().
            response();
    }

    @Test
    void getAllOrders_StatusCode_200() {
        given().
            contentType("application/json").
            accept("application/json").
        when().
            get("/api/order").
        then().
            statusCode(200).
            contentType("application/json").
            extract().
            response();
    }

    @Test
    @Order(2)
    void getAllOrders() {
        Response response =
                given().
                    contentType("application/json").
                    accept("application/json").
                when().
                    get("/api/product").
                then().
                    statusCode(200).
                    contentType("application/json").
                    extract().
                    response();

        assertNotNull(response);
        assertEquals(1, response.jsonPath().getList("").size());
    }

    @Test
    @Order(3)
    void getOrder_statusCode_200() {
        given().
            contentType("application/json").
            accept("application/json").
        when().
            get("/api/order/1").
        then().
            statusCode(200).
            contentType("application/json").
            extract().
            response();
    }

    @Test
    @Order(4)
    void getOrder() {
        Response response =
                given().
                    contentType("application/json").
                    accept("application/json").
                when().
                    get("/api/order/1").
                then().
                    statusCode(200).
                    contentType("application/json").
                    extract().
                    response();

        String id = response.jsonPath().getString("id");
        Integer employeeId = Integer.parseInt(response.jsonPath().getString("employeeId"));
        String dateTime = response.jsonPath().getString("dateTime");

        assertNotNull(id);
        assertEquals(1, employeeId);
        assertEquals(LocalDateTime.of(2021, 1, 1, 12, 30, 15).toString(), dateTime);
    }

    @Test
    void getOrder_OrderNotFoundException_500() {
        given().
            contentType("application/json").
            accept("application/json").
        when().
            get("/api/order/10").
        then().
            statusCode(500).
            contentType("application/json").
            extract().
            response();
    }

    @Test
    @Order(5)
    void updateOrder_StatusCode_200() {
        List<OrderItemRequest> orderItemListReq = new ArrayList<>();
        orderItemListReq.add(orderItemRequest);

        OrderRequest updateOrderRequest = OrderRequest.builder()
                .orderItemList(orderItemListReq)
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .employeeId(10)
                .build();

        Response response =
                given().
                    contentType("application/json").
                    accept("application/json").
                    body(updateOrderRequest).
                when().
                    put("/api/order/1").
                then().
                    statusCode(200).
                    contentType("application/json").
                    extract().
                    response();

        String id = response.jsonPath().getString("id");
        Integer employeeId = Integer.parseInt(response.jsonPath().getString("employeeId"));
        String dateTime = response.jsonPath().getString("dateTime");

        assertNotNull(id);
        assertEquals(10, employeeId);
        assertEquals(LocalDateTime.of(2021, 1, 1, 12, 30, 15).toString(), dateTime);
    }

    @Test
    void updateOrder_OrderNotFoundException_400() {
        List<OrderItemRequest> orderItemListReq = new ArrayList<>();
        orderItemListReq.add(orderItemRequest);

        OrderRequest updateOrderRequest = OrderRequest.builder()
                .orderItemList(orderItemListReq)
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .employeeId(10)
                .build();

        given().
            contentType("application/json").
            accept("application/json").
            body(updateOrderRequest).
        when().
            put("/api/product/100").
        then().
            statusCode(400).
            contentType("application/json").
            extract().
            response();
    }

    @Test
    void updateProduct_ArgumentNotValidException_400() {
        List<OrderItemRequest> orderItemListReq = new ArrayList<>();
        orderItemListReq.add(orderItemRequest);

        OrderRequest updateOrderRequest = OrderRequest.builder()
                .orderItemList(orderItemListReq)
                .dateTime(null)
                .employeeId(10)
                .build();

        given().
            contentType("application/json").
            accept("application/json").
            body(updateOrderRequest).
        when().
            put("/api/order/1").
        then().
            statusCode(400).
            contentType("application/json").
            extract().
            response();
    }

    @Test
    void updateOrder_NotEnoughException_400() {
        List<OrderItemRequest> orderItemListReq = new ArrayList<>();
        orderItemListReq.add(orderItemRequest);

        OrderRequest updateOrderRequest = OrderRequest.builder()
                .orderItemList(orderItemListReq)
                .build();

        given().
            contentType("application/json").
            accept("application/json").
            body(updateOrderRequest).
        when().
            put("/api/order/1").
        then().
            statusCode(400).
            contentType("application/json").
            extract().
            response();
    }

    @Test
    @Order(6)
    void deleteOrder_StatusCode_200() {
        given().
            contentType("application/json").
            accept("application/json").
        when().
            delete("/api/order/1").
        then().
            statusCode(200).
            extract().
            response();
    }

    @Test
    void deleteOrder_OrderNotFoundException_500() {
        given().
            contentType("application/json").
            accept("application/json").
        when().
            delete("/api/product/10").
        then().
            statusCode(500).
            extract().
            response();
    }
}
