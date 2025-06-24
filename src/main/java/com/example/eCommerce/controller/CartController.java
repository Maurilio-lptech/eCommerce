package com.example.eCommerce.controller;


import com.example.eCommerce.dto.OrderDetailsDto;
import com.example.eCommerce.dto.OrderDto;
import com.example.eCommerce.enums.OrderState;
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

    private final OrderService orderService;
    private final OrderDetailsService DetailsServiceService;

    //Todo:createCart
    //Todo: trasform cart to order check out
    //Todo:addToCart
    //todo: removeFromCart
    //Todo: svuotare il carello

    @PostMapping("/new/{customerId}")
    public ResponseEntity<OrderDto> createCart(@PathVariable  UUID customerId){
        OrderDto cartOrder=new OrderDto();
        cartOrder.setState(OrderState.NEL_CARRELLO.toString());
        cartOrder.setCustomer_id(customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(cartOrder));
    }


    @PostMapping("/add/{customerId}/")
    public ResponseEntity<OrderDto> addToCart(@PathVariable  UUID customerId, @RequestBody OrderDetailsDto orderDetailsDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.addToCart(customerId , orderDetailsDto));
    }


    //add to cart
    //inserire un order details dentro il carello nel db esistente nel db
    //check se l'utente è valido
    // check se esiste il carello del utente --> altrimenti lo si crea
    //check quantity>0











}
