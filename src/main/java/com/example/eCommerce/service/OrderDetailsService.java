package com.example.eCommerce.service;

import com.example.eCommerce.dto.OrderDetailsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface OrderDetailsService {

    //CRUD
    public OrderDetailsDto createOrderDetails(OrderDetailsDto orderDetailsDto);

    public void deleteOrderDetails(UUID id);

    public OrderDetailsDto updateOrderDetails(UUID id, OrderDetailsDto orderDetailsDto);

    public Page<OrderDetailsDto> getAllOrderDetails(Pageable pageable);
    public OrderDetailsDto getOrderDetailsById(UUID id);
    //Other
    public List<OrderDetailsDto> getAllOrderDetailsByOrderId(UUID orderId);

    //Cart
    public List<OrderDetailsDto> getAllCartOrderDetails();







}
