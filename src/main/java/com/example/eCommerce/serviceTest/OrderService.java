package com.example.eCommerce.serviceTest;

import com.example.eCommerce.dto.OrderDto;
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

    //cart
    public OrderDto getCart(UUID customerId);

    //User order
    public Page<OrderDto> getAllUserOrdersByState(UUID customerId, String state, Pageable pageable);

    public OrderDto getUserOrderById(UUID customerId, UUID orderId);
    Page<OrderDto> getAllUserOrders(UUID customerId, Pageable pageable);
}
