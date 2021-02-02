package com.bar.bill.controller;

import com.bar.bill.request.BillRequest;
import com.bar.bill.service.BillService;
import com.bar.bill.service.mapper.BillMapperService;
import com.bar.order.request.OrderRequest;
import com.bar.orderitems.request.OrderItemRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = DEFINED_PORT)
class BillControllerTest {

    @InjectMocks
    BillController billController;

    @Mock
    BillService billService;
    @Mock
    BillMapperService billMapperService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test()
    @Order(1)
    void before() {
        // add Product
        Map<String, Object> newProduct = new HashMap<>();
        newProduct.put("name", "test1");
        newProduct.put("priceNet", 2.00);

        given().
                contentType("application/json").
                accept("application/json").
                body(newProduct).
                when().
                post("/api/product");

        // add Order
        OrderItemRequest orderItemRequest = OrderItemRequest.builder()
                .productId(1L)
                .quantity(10)
                .build();

        List<OrderItemRequest> orderItemListReq = new ArrayList<>();
        orderItemListReq.add(orderItemRequest);

        OrderRequest orderRequest = OrderRequest.builder()
                .orderItemList(orderItemListReq)
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .employeeId(1)
                .build();

        given().
                contentType("application/json").
                accept("application/json").
                body(orderRequest).
                when().
                post("/api/order");
    }

    @Test()
    @Order(2)
    void addBill_StatusCode_201() {
        BillRequest billRequest = BillRequest.builder()
                .orderId(1L)
                .company("test")
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .customerName("testCustomer")
                .nipNumber("111-111-11-11")
                .build();

        Response response =
                given().
                        contentType("application/json").
                        accept("application/json").
                        body(billRequest).
                        when().
                        post("/api/bill").
                        then().
                        statusCode(201).
                        contentType("application/json").
                        extract().
                        response();

        String id = response.jsonPath().getString("id");
        String company = response.jsonPath().getString("company");
        String dateTime = response.jsonPath().getString("dateTime");
        String customerName = response.jsonPath().getString("customerName");
        String nipNumber = response.jsonPath().getString("nipNumber");

        assertNotNull(id);
        assertEquals("test", company);
        assertEquals(LocalDateTime.of(2021, 1, 1, 12, 30, 15).toString(), dateTime);
        assertEquals("testCustomer", customerName);
        assertEquals("111-111-11-11", nipNumber);
    }

    @Test()
    void addBill_NotEnoughParametersException_400() {
        BillRequest billRequest = BillRequest.builder()
                .orderId(1L)
                .company("test")
                .customerName("testCustomer")
                .nipNumber("111-111-11-11")
                .build();

        given().
                contentType("application/json").
                accept("application/json").
                body(billRequest).
                when().
                post("/api/bill").
                then().
                statusCode(400).
                contentType("application/json").
                extract().
                response();
    }

    @Test()
    void addBill_ArgumentNotValidException_400() {
        BillRequest billRequest = BillRequest.builder()
                .orderId(1L)
                .company("test")
                .dateTime(null)
                .customerName("testCustomer")
                .nipNumber("111-111-11-11")
                .build();

        given().
                contentType("application/json").
                accept("application/json").
                body(billRequest).
                when().
                post("/api/bill").
                then().
                statusCode(400).
                contentType("application/json").
                extract().
                response();
    }

    @Test
    void getAllBills_StatusCode_200() {
        given().
                contentType("application/json").
                accept("application/json").
                when().
                get("/api/bill").
                then().
                statusCode(200).
                contentType("application/json").
                extract().
                response();
    }

    @Test
    @Order(3)
    void getAllBills() {
        Response response =
                given().
                        contentType("application/json").
                        accept("application/json").
                        when().
                        get("/api/bill").
                        then().
                        statusCode(200).
                        contentType("application/json").
                        extract().
                        response();

        assertNotNull(response);
        assertEquals(1, response.jsonPath().getList("").size());
    }

    @Test
    @Order(4)
    void getBill_statusCode_200() {
        given().
                contentType("application/json").
                accept("application/json").
                when().
                get("/api/bill/1").
                then().
                statusCode(200).
                contentType("application/json").
                extract().
                response();
    }

    @Test
    @Order(5)
    void getBill() {
        Response response =
                given().
                        contentType("application/json").
                        accept("application/json").
                        when().
                        get("/api/bill/1").
                        then().
                        statusCode(200).
                        contentType("application/json").
                        extract().
                        response();

        String id = response.jsonPath().getString("id");
        String company = response.jsonPath().getString("company");
        String dateTime = response.jsonPath().getString("dateTime");
        String customerName = response.jsonPath().getString("customerName");
        String nipNumber = response.jsonPath().getString("nipNumber");

        assertNotNull(id);
        assertEquals("test", company);
        assertEquals(LocalDateTime.of(2021, 1, 1, 12, 30, 15).toString(), dateTime);
        assertEquals("testCustomer", customerName);
        assertEquals("111-111-11-11", nipNumber);
    }

    @Test
    void getBill_BillNotFoundException_500() {
        given().
                contentType("application/json").
                accept("application/json").
                when().
                get("/api/bill/10").
                then().
                statusCode(500).
                contentType("application/json").
                extract().
                response();
    }

    @Test
    @Order(6)
    void updateBill_StatusCode_200() {
        BillRequest updateBillRequest = BillRequest.builder()
                .orderId(1L)
                .company("updateTest")
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .customerName("updateTestCustomer")
                .nipNumber("111-111-11-11")
                .build();

        Response response =
                given().
                        contentType("application/json").
                        accept("application/json").
                        body(updateBillRequest).
                        when().
                        put("/api/bill/1").
                        then().
                        statusCode(200).
                        contentType("application/json").
                        extract().
                        response();

        String id = response.jsonPath().getString("id");
        String company = response.jsonPath().getString("company");
        String dateTime = response.jsonPath().getString("dateTime");
        String customerName = response.jsonPath().getString("customerName");
        String nipNumber = response.jsonPath().getString("nipNumber");

        assertNotNull(id);
        assertEquals("updateTest", company);
        assertEquals(LocalDateTime.of(2021, 1, 1, 12, 30, 15).toString(), dateTime);
        assertEquals("updateTestCustomer", customerName);
        assertEquals("111-111-11-11", nipNumber);
    }

    @Test
    void updateBill_BillNotFoundException_500() {
        BillRequest updateBillRequest = BillRequest.builder()
                .orderId(1L)
                .company("updateTest")
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .customerName("updateTestCustomer")
                .nipNumber("111-111-11-11")
                .build();

        given().
                contentType("application/json").
                accept("application/json").
                body(updateBillRequest).
                when().
                put("/api/bill/10").
                then().
                statusCode(500).
                contentType("application/json").
                extract().
                response();
    }

    @Test
    void updateBill_ArgumentNotValidException_400() {
        BillRequest updateBillRequest = BillRequest.builder()
                .orderId(1L)
                .company("updateTest")
                .dateTime(null)
                .customerName("updateTestCustomer")
                .nipNumber("111-111-11-11")
                .build();

        given().
                contentType("application/json").
                accept("application/json").
                body(updateBillRequest).
                when().
                put("/api/bill/1").
                then().
                statusCode(400).
                contentType("application/json").
                extract().
                response();
    }

    @Test
    void updateBill_NotEnoughParametersException_400() {
        BillRequest updateBillRequest = BillRequest.builder()
                .orderId(1L)
                .customerName("updateTestCustomer")
                .nipNumber("111-111-11-11")
                .build();

        given().
                contentType("application/json").
                accept("application/json").
                body(updateBillRequest).
                when().
                put("/api/bill/1").
                then().
                statusCode(400).
                contentType("application/json").
                extract().
                response();
    }

    @Test
    @Order(7)
    void deleteBill_StatusCode_200() {
        given().
                contentType("application/json").
                accept("application/json").
                when().
                delete("/api/bill/1").
                then().
                statusCode(200).
                extract().
                response();
    }

    @Test
    void deleteBill_BillNotFoundException_500() {
        given().
                contentType("application/json").
                accept("application/json").
                when().
                delete("/api/bill/10").
                then().
                statusCode(500).
                extract().
                response();
    }
}

//@ExtendWith(SpringExtension.class)

