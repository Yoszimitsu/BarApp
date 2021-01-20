package com.bar.product.controller;

import com.bar.product.dto.ProductDto;
import com.bar.product.mapper.ProductMapper;
import com.bar.product.request.ProductRequest;
import com.bar.product.service.ProductService;
import com.bar.system.endpoint.Endpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(ProductController.PRODUCT_ENDPOINT)
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    static final String PRODUCT_ENDPOINT = Endpoint.API_ROOT + Endpoint.URN_PRODUCT;

    @Autowired
    ProductService productService;
    @Autowired
    ProductMapper productMapper;

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ProductDto get(@PathVariable long id) {
        log.info("GET " + PRODUCT_ENDPOINT + "/" + id);
        return productMapper.mapToDto(productService.get(id));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    List<ProductDto> getAll() {
        log.info("GET " + PRODUCT_ENDPOINT);
        return productMapper.mapToDtoList(productService.getALl());
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<ProductDto> post(@Valid @RequestBody ProductRequest productRequest) {
        log.info("POST " + PRODUCT_ENDPOINT);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productMapper.mapToDto(productService.add(productRequest)));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ProductDto put(@PathVariable long id, @Valid @RequestBody ProductRequest productRequest) {
        log.info("PUT " + PRODUCT_ENDPOINT + "/" + id);
        return productMapper.mapToDto(productService.update(id, productRequest));
    }

    @DeleteMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    void delete(@PathVariable long id) {
        log.info("DELETE " + PRODUCT_ENDPOINT + "/" + id);
        productService.delete(id);
    }
}
