package com.example.eCommerce.mapper;

import com.example.eCommerce.dto.CategoryDto;
import com.example.eCommerce.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    public CategoryDto toDto(Category category);

    public Category toEntity(CategoryDto categoryDto);

}
