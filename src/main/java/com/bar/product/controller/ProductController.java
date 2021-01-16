package com.bar.product.controller;

import com.bar.product.dto.ProductDto;
import com.bar.product.mapper.ProductMapper;
import com.bar.product.request.ProductRequest;
import com.bar.product.service.ProductService;
import com.bar.system.endpoint.Endpoint;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ProductController.PRODUCT_ENDPOINT)
@AllArgsConstructor
@Slf4j
public class ProductController {

    protected static final String PRODUCT_ENDPOINT = Endpoint.API_ROOT + Endpoint.URN_PRODUCT;
    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping(value = "/{id}")
    ProductDto get(@PathVariable long id) {
        log.info("GET " + PRODUCT_ENDPOINT + "/" + id);
        return productMapper.mapToDto(productService.get(id));
    }

    @GetMapping
    List<ProductDto> getAll() {
        log.info("GET " + PRODUCT_ENDPOINT);
        return productMapper.mapToDtoList(productService.getALl());
    }

    @PostMapping()
    ResponseEntity<ProductDto> post(@Valid @RequestBody ProductRequest productRequest) {
        log.info("POST " + PRODUCT_ENDPOINT);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productMapper.mapToDto(productService.add(productRequest)));
    }

    @PutMapping(value = "/{id}")
    ProductDto put(@PathVariable long id,
                   @Valid @RequestBody ProductRequest productRequest) {
        log.info("PUT " + PRODUCT_ENDPOINT + "/" + id);
        return productMapper.mapToDto(productService.update(id, productRequest));
    }

    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable long id) {
        log.info("DELETE " + PRODUCT_ENDPOINT + "/" + id);
        productService.delete(id);
    }
}
