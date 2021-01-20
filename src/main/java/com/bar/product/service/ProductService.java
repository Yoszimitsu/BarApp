package com.bar.product.service;

import com.bar.product.entity.Product;
import com.bar.product.repository.ProductRepository;
import com.bar.product.request.ProductRequest;
import com.bar.system.error.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Product get(long id) {
        return Optional.ofNullable(productRepository.findById(id)).get()
                .orElseThrow(() -> new NotFoundException("Product not found", Long.toString(id)));
    }

    public List<Product> getALl() {
        return productRepository.findAll();
    }

    public Product add(ProductRequest productRequest) {
        var product = Product.builder()
                .name(productRequest.getName())
                .priceNet(productRequest.getPriceNet())
                .build();
        return productRepository.save(product);
    }

    public Product update(long id, ProductRequest productRequest) {
        checkIfProductExistsInDatabase(id);
        var product = Product.builder()
                .id(id)
                .name(productRequest.getName())
                .priceNet(productRequest.getPriceNet())
                .build();
        return productRepository.save(product);
    }

    public void delete(long id) {
        checkIfProductExistsInDatabase(id);
        productRepository.deleteById(id);
    }

    private void checkIfProductExistsInDatabase(long id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Product not found", Long.toString(id));
        }
    }
}
