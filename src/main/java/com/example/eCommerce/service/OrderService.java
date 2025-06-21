package com.example.eCommerce.service;

import com.example.eCommerce.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService {


    //Crud
    public OrderDto createOrder(OrderDto orderDto);

    public void deleteOrder(UUID id);

    // public OrderDto updateOrder(UUID id, OrderDto orderDto);

    //by id of user
    public Page<OrderDto> getAllOrders(Pageable pageable);

    public OrderDto getOrderById(UUID id);









}
