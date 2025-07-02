package com.example.eCommerce.controller;

import com.example.eCommerce.dto.OrderDetailsDto;
import com.example.eCommerce.dto.OrderDto;
import com.example.eCommerce.securitySpring.security.services.UserDetailsImpl;
import com.example.eCommerce.service.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    // svuotare il carrello

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderDto> getCart() {
        UUID customerId=getCurrentUserId();
        return ResponseEntity.ok(service.getCart(customerId));
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderDto> createCart() {
        UUID customerId = getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createCart(customerId));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderDto> addToCart(@RequestBody OrderDetailsDto orderDetailsDto) {
        UUID customerId = getCurrentUserId();
        return ResponseEntity.ok(service.addToCart(customerId, orderDetailsDto));
    }

    @PostMapping("/remove")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderDto> removeFromCart(@RequestBody OrderDetailsDto orderDetailsDto) {
        UUID customerId = getCurrentUserId();
        return ResponseEntity.ok(service.removeFromCart(customerId, orderDetailsDto));
    }

    @PostMapping("/clear")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> clearCart() {
        UUID customerId = getCurrentUserId();
        service.clearCart(customerId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/checkout")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderDto> checkout(@RequestBody OrderDto cart) {
        cart.setCustomer_id(getCurrentUserId());
        return ResponseEntity.ok(service.checkout(cart));
    }


}
