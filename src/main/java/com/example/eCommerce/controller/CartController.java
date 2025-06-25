package com.example.eCommerce.controller;


import com.example.eCommerce.dto.OrderDetailsDto;
import com.example.eCommerce.dto.OrderDto;
import com.example.eCommerce.enums.OrderState;
import com.example.eCommerce.service.CartServiceImpl;
import com.example.eCommerce.service.OrderDetailsService;
import com.example.eCommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CartController {

    private final CartServiceImpl service;

    //createCart
    //trasform cart to order check out
    //addToCart
    // removeFromCart
    // svuotare il carello

    @GetMapping("/{customerId}/")
    public ResponseEntity<OrderDto> getCart(@PathVariable  UUID customerId){
        return ResponseEntity.ok(service.getCart(customerId));
    }

    @PostMapping("/new/{customerId}/")
    public ResponseEntity<OrderDto> createCart(@PathVariable  UUID customerId){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createCart(customerId));
    }

    @PostMapping("/add/{customerId}/")
    public ResponseEntity<OrderDto> addToCart(@PathVariable  UUID customerId, @RequestBody OrderDetailsDto orderDetailsDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addToCart(customerId , orderDetailsDto));
    }

    @PostMapping("/remove/{customerId}/")
    public ResponseEntity<OrderDto> removeFromCart(@PathVariable  UUID customerId, @RequestBody OrderDetailsDto orderDetailsDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.removeFromCart(customerId , orderDetailsDto));
    }


    @PostMapping("/clear/{customerId}/")
    public ResponseEntity<Void> clearCart(@PathVariable  UUID customerId){
        service.clearCart(customerId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/checkout/{customerId}/")
    public ResponseEntity<OrderDto> checkout(@PathVariable  UUID customerId){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.checkout(customerId));
    }





}
