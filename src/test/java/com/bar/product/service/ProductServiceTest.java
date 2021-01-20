package com.bar.product.service;

import com.bar.product.entity.Product;
import com.bar.product.repository.ProductRepository;
import com.bar.product.request.ProductRequest;
import com.bar.system.error.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @BeforeEach
    public void onSetUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getProduct() {
        Product product = Product.builder()
                .id(1L)
                .name("test")
                .priceNet(1.23)
                .build();

        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(product));

        Product result = productService.get(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("test", result.getName());
        assertEquals(1.23, result.getPriceNet());
    }

    @Test
    void getProduct_NotFoundException() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        assertThrows(NotFoundException.class,
                () -> {
                    productService.get(1);
                }
        );
    }

    @Test
    void getALlProducts() {
        List<Product> productList = new ArrayList<>();
        Product product1 = Product.builder()
                .id(1L)
                .name("test1")
                .priceNet(1.23)
                .build();
        Product product2 = Product.builder()
                .id(2L)
                .name("test2")
                .priceNet(1.23)
                .build();

        productList.add(product1);
        productList.add(product2);

        when(productRepository.findAll()).thenReturn(productList);

        List<Product> result = productService.getALl();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("test1", result.get(0).getName());
        assertEquals("test2", result.get(1).getName());
    }

    @Test
    void addProduct() {
        Product product = Product.builder()
                .id(1L)
                .name("test")
                .priceNet(1.23)
                .build();

        ProductRequest productRequest = ProductRequest.builder()
                .name("test")
                .priceNet(1.23)
                .build();

        when(productRepository.save(any())).thenReturn(product);

        Product result = productService.add(productRequest);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("test", result.getName());
        assertEquals(1.23, result.getPriceNet());
    }

    @Test
    void updateProduct() {
        long id = 1;
        Product product = Product.builder()
                .id(1L)
                .name("updatedTest")
                .priceNet(1.23)
                .build();
        ProductRequest productRequest = ProductRequest.builder()
                .name("updatedTest")
                .priceNet(1.23)
                .build();

        when(productRepository.save(any())).thenReturn(product);
        when(productRepository.existsById(anyLong())).thenReturn(true);

        Product result = productService.update(id, productRequest);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("updatedTest", result.getName());
        assertEquals(1.23, result.getPriceNet());
    }

    @Test
    void updateProduct_ProductNotFound() {
        long id = 1;
        ProductRequest productRequest = ProductRequest.builder()
                .name("updatedTest")
                .priceNet(1.23)
                .build();

        when(productRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> {
                    productService.update(id, productRequest);
                }
        );
    }

    @Test
    void deleteProduct() {
        long id = 1;
        when(productRepository.existsById(anyLong())).thenReturn(true);

        productService.delete(1);

        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteProduct_ProductNotFound() {
        long id = 1;
        when(productRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> {
                    productService.delete(id);
                });
    }
}
