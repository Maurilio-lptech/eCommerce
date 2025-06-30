package com.example.eCommerce.serviceTest;

import com.example.eCommerce.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface ProductService {

    //Crud
    public ProductDto createProduct(ProductDto productDto, MultipartFile image) throws IOException;

    public void deleteProduct(UUID id);

    public ProductDto updateProduct(ProductDto productDto, MultipartFile image)throws IOException;

    public Page<ProductDto> getAllProducts(Pageable pageable);

    //other

    public ProductDto getProductById(UUID id);

    public Page<ProductDto> getAllProductByName(String name, Pageable pageable);

    public Page<ProductDto> searchProduct(String name, String category, Double price, Pageable pageable );
}
