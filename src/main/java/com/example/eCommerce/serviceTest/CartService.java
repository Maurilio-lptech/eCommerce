package com.example.eCommerce.serviceTest;

import com.example.eCommerce.dto.OrderDetailsDto;
import com.example.eCommerce.dto.OrderDto;

import java.util.UUID;

public interface CartService {

    public OrderDto createCart(UUID customerId);

    public OrderDto addToCart(UUID customerId, OrderDetailsDto orderDetailsDto);

    public OrderDto removeFromCart(UUID customerId, OrderDetailsDto orderDetailsDto);

    public void clearCart(UUID customerId);

    public OrderDto checkout(OrderDto orderDto);

    public OrderDto getCart(UUID customerId);
}
