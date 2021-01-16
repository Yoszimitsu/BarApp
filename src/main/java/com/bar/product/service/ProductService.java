package com.bar.product.service;

import com.bar.product.entity.Product;
import com.bar.product.repository.ProductRepository;
import com.bar.product.request.ProductRequest;
import com.bar.system.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product get(long productId) {
        return Optional.of(productRepository.findById(productId).get())
                .orElseThrow(() -> new NotFoundException("Product not found", Long.toString(productId)));
    }

    public List<Product> getALl() {
        return productRepository.findAll();
    }

    public Product add(ProductRequest productRequest) {
        var product = Product.builder()
                .name(productRequest.getName())
                .price_net(productRequest.getPrice_net())
                .build();
        return productRepository.save(product);
    }

    public Product update(long productId, ProductRequest productRequest) {
        checkIfProductExistsInDatabase(productId);
        var product = Product.builder()
                .name(productRequest.getName())
                .price_net(productRequest.getPrice_net())
                .build();
        return productRepository.save(product);
    }

    public void delete(long productId) {
        checkIfProductExistsInDatabase(productId);
        productRepository.deleteById(productId);
    }

    private void checkIfProductExistsInDatabase(long productId) {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException("CommunityMeeting not found", Long.toString(productId));
        }
    }
}
