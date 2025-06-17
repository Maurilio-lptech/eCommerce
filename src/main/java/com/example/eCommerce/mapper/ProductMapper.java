package com.example.eCommerce.mapper;

import com.example.eCommerce.dto.ProductDto;
import com.example.eCommerce.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

//uses = {CategoryMapper.class, UserMapper.class}
@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "seller_id", source = "seller.id")
    @Mapping(target = "category_id", source = "category.id")
    @Mapping(target = "sellerName", source = "seller.name")
    @Mapping(target = "categoryName", source = "category.name")
    public ProductDto toDto(Product product);

    public Product toEntity(ProductDto productDto);
}
