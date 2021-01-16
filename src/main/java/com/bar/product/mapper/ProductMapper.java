package com.bar.product.mapper;

import com.bar.product.dto.ProductDto;
import com.bar.product.entity.Product;
import com.bar.product.request.ProductRequest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    Product mapToEntity(ProductRequest productRequest);

    ProductDto mapToDto(Product product);

    List<ProductDto> mapToDtoList(List<Product> productList);
}
