package com.example.eCommerce.mapper;


import com.example.eCommerce.dto.OrderDto;

import com.example.eCommerce.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderDetailsMapper.class, UserMapper.class})
public interface OrderMapper {

    @Mapping(target = "customer_id", source = "customer.id")
    public OrderDto toDto(Order order);


    @Mapping(target = "orderDetailsList", source = "orderDetailsList")
    public Order toEntity(OrderDto orderDto);

}
