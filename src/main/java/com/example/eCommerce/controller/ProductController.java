package com.example.eCommerce.controller;

import com.example.eCommerce.dto.ProductDto;
import com.example.eCommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService service;

    //CRUD
    @PostMapping("/new")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto EntityToCreate) {
        if (EntityToCreate.getId() != null) {
            EntityToCreate.setId(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProduct(EntityToCreate));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable UUID id, @RequestBody ProductDto ProductToUpdate) {
        return ResponseEntity.ok(service.updateProduct(id, ProductToUpdate));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getProductById(id));
    }

    @GetMapping("/search")
    public Page<ProductDto> findProductByName(@RequestParam String name, Pageable pageable) {
        return service.getAllProductByName(name, pageable);
    }

    @GetMapping("/Products")
    public Page<ProductDto> findAllProducts(Pageable pageable) {
        return service.getAllProducts(pageable);
    }


    @GetMapping("/search2")
    public Page<ProductDto> searchProduct(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double price,
            Pageable pageable
    ) {
        return service.searchProduct(name,category,price,pageable);
    }


}
