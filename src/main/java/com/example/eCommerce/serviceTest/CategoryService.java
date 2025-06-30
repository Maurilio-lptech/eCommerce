package com.example.eCommerce.serviceTest;

import com.example.eCommerce.dto.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;


public interface CategoryService {

    //Crud
    public CategoryDto createCategory(CategoryDto categoryDto);

    public void deleteCategory(UUID id);

    public CategoryDto updateCategory(UUID id, CategoryDto categoryDto);

    public Page<CategoryDto> getAllCategories(Pageable pageable);

    public CategoryDto getCategoryById(UUID id);

}
