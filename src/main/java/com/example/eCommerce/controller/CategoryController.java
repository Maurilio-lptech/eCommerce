package com.example.eCommerce.controller;

import com.example.eCommerce.dto.CategoryDto;
import com.example.eCommerce.serviceTest.CategoryService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/category")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService service;

    //CRUD
    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody @NotNull CategoryDto EntityToCreate){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createCategory(EntityToCreate));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable UUID id ,@RequestBody @NotNull CategoryDto CategoryToUpdate){
        return ResponseEntity.ok(service.updateCategory(id,CategoryToUpdate));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable  UUID id){
        service.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> findCategoryById(@PathVariable UUID id){
        return ResponseEntity.ok(service.getCategoryById(id));
    }

    @GetMapping("/categories")
    public Page<CategoryDto> findAllCategories(Pageable pageable){
        return service.getAllCategories(pageable);
    }






}
