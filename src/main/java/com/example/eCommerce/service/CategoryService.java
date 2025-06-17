package com.example.eCommerce.service;

import com.example.eCommerce.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


public interface CategoryService {

    //Crud
    public CategoryDto createCategory(CategoryDto categoryDto);

    public void deleteCategory(UUID id);

    public CategoryDto updateCategory(UUID id, CategoryDto categoryDto);

    public Page<CategoryDto> getAllCategories(Pageable pageable);

    public CategoryDto getCategoryById(UUID id);

}
