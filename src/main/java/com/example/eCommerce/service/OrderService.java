package com.example.eCommerce.service;

import com.example.eCommerce.dto.OrderDto;
import com.example.eCommerce.entity.Order;
import com.example.eCommerce.enums.OrderState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OrderService {


    //Crud
    public OrderDto createOrder(OrderDto orderDto);

    public void deleteOrder(UUID id);

     public OrderDto updateOrder(OrderDto orderDto);

    //by id of user
    public Page<OrderDto> getAllOrders(Pageable pageable);

    public OrderDto getOrderById(UUID id);

    public List<OrderDto> getAllOrdersByState(String state);









}
