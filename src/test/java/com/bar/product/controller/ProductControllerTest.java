package com.bar.product.controller;

import com.bar.product.mapper.ProductMapper;
import com.bar.product.service.ProductService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
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

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerTest {

    @InjectMocks
    ProductController productController;

    @Mock
    ProductService productService;
    @Mock
    ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test()
    @Order(1)
    void addProduct_StatusCode_201() {
        Map<String, Object> newProduct = new HashMap<>();
        newProduct.put("name", "test1");
        newProduct.put("priceNet", 2.00);

        Response response =
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

        String id = response.jsonPath().getString("id");
        String name = response.jsonPath().getString("name");
        Double priceNet = Double.parseDouble(response.jsonPath().getString("priceNet"));

        assertNotNull(id);
        assertEquals("test1", name);
        assertEquals(2.00, priceNet);
    }

    @Test()
    @Order(2)
    void addProduct_ProductExists_500() {
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
            statusCode(500).
            extract().
            response();
    }

    @Test()
    void addProduct_ArgumentNotValidException_400() {
        Map<String, Object> newProduct = new HashMap<>();
        newProduct.put("name_wrongLabel", "test1");
        newProduct.put("price_Net", 2.00);

        given().
            contentType("application/json").
            accept("application/json").
            body(newProduct).
        when().
            post("/api/product").
        then().
            statusCode(400).
            extract().
            response();
    }

    @Test()
    void addProduct_notEnoughArgument_400() {
        Map<String, Object> newProduct = new HashMap<>();
        newProduct.put("name", "test2");

        given().
            contentType("application/json").
            accept("application/json").
            body(newProduct).
        when().
            post("/api/product").
        then().
            statusCode(400).
            extract().
            response();
    }

    @Test
    void getAllProducts_StatusCode_200() {
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
    }

    @Test
    @Order(3)
    void getAllProducts() {
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
    @Order(4)
    void getProduct_statusCode_200() {
            given().
                contentType("application/json").
                accept("application/json").
            when().
                get("/api/product/1").
            then().
                statusCode(200).
                contentType("application/json").
                extract().
                response();
    }

    @Test
    @Order(5)
    void getProduct() {
        Response response =
                given().
                    contentType("application/json").
                    accept("application/json").
                when().
                    get("/api/product/1").
                then().
                    statusCode(200).
                    contentType("application/json").
                    extract().
                    response();

        String id = response.jsonPath().getString("id");
        String name = response.jsonPath().getString("name");
        Double priceNet = Double.parseDouble(response.jsonPath().getString("priceNet"));

        assertNotNull(id);
        assertEquals("test1", name);
        assertEquals(2.00, priceNet);
    }

    @Test
    void getProduct_ProductNotFoundException_500() {
            given().
                contentType("application/json").
                accept("application/json").
            when().
                get("/api/product/10").
            then().
                statusCode(500).
                contentType("application/json").
                extract().
                response();
    }

    @Test
    @Order(6)
    void updateProduct_StatusCode_200() {
        Map<String, Object> updateProduct = new HashMap<>();
        updateProduct.put("name", "updateTest");
        updateProduct.put("priceNet", 4.00);

        Response response =
                given().
                    contentType("application/json").
                    accept("application/json").
                    body(updateProduct).
                when().
                    put("/api/product/1").
                then().
                    statusCode(200).
                    contentType("application/json").
                    extract().
                    response();

        String id = response.jsonPath().getString("id");
        String name = response.jsonPath().getString("name");
        Double priceNet = Double.parseDouble(response.jsonPath().getString("priceNet"));

        assertNotNull(id);
        assertEquals("updateTest", name);
        assertEquals(4.00, priceNet);
    }

    @Test
    void updateProduct_ProductNotFoundException_500() {
        Map<String, Object> updateProduct = new HashMap<>();
        updateProduct.put("name", "updateTest");
        updateProduct.put("priceNet", 4.00);

        given().
            contentType("application/json").
            accept("application/json").
            body(updateProduct).
        when().
            put("/api/product/10").
        then().
            statusCode(500).
            contentType("application/json").
            extract().
            response();
    }

    @Test
    void updateProduct_ArgumentNotValidException_400() {
        Map<String, Object> updateProduct = new HashMap<>();
        updateProduct.put("name_wrongLabel", "updateTest");
        updateProduct.put("priceNet", 4.00);

        given().
            contentType("application/json").
            accept("application/json").
            body(updateProduct).
        when().
            put("/api/product/1").
        then().
            statusCode(400).
            contentType("application/json").
            extract().
            response();
    }

    @Test
    void updateProduct_NotEnoughException_400() {
        Map<String, Object> updateProduct = new HashMap<>();
        updateProduct.put("name", "updateTest");

        given().
            contentType("application/json").
            accept("application/json").
            body(updateProduct).
        when().
            put("/api/product/1").
        then().
            statusCode(400).
            contentType("application/json").
            extract().
            response();
    }

    @Test
    @Order(7)
    void deleteProduct_StatusCode_200() {
        given().
            contentType("application/json").
            accept("application/json").
        when().
            delete("/api/product/1").
        then().
            statusCode(200).
            extract().
            response();
    }

    @Test
    void deleteProduct_ProductNotFoundException_500() {
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
