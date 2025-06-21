package com.example.eCommerce.mapper;

import com.example.eCommerce.dto.OrderDetailsDto;
import com.example.eCommerce.entity.OrderDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderDetailsMapper {

    @Mapping(target = "order_id", source = "order.id")
    @Mapping(target = "product_id", source = "product.id")
    @Mapping(target = "subtotal", expression = "java(orderDetails.getPriceForUnit() * orderDetails.getQuantity())")
    public OrderDetailsDto toDto(OrderDetails orderDetails);

    @Mapping(target = "order", ignore = true) // Gestito separatamente nel service
    @Mapping(target = "product", ignore = true) // Gestito separatamente nel service
    public OrderDetails toEntity(OrderDetailsDto orderDetailsDtoDto);
}
