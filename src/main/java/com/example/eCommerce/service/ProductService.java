package com.example.eCommerce.service;

import com.example.eCommerce.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {

    //Crud
    public ProductDto createProduct(ProductDto productDto);

    public void deleteProduct(UUID id);

    public ProductDto updateProduct(UUID id, ProductDto productDto);

    public Page<ProductDto> getAllProducts(Pageable pageable);

    public ProductDto getProductById(UUID id);

    public Page<ProductDto> getAllProductByName(String name, Pageable pageable);

    public Page<ProductDto> searchProduct(String name, String category, Double price, Pageable pageable );
}
