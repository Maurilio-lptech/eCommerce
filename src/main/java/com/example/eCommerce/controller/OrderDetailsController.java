package com.example.eCommerce.controller;

import com.example.eCommerce.dto.OrderDetailsDto;
import com.example.eCommerce.service.OrderDetailsService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/orderDetails")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderDetailsController {

    private final OrderDetailsService service;

    //CRUD
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public ResponseEntity<OrderDetailsDto> createCategory(@RequestBody OrderDetailsDto orderDetailsDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createOrderDetails(orderDetailsDto));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDetailsDto> updateOrderDetails(@PathVariable UUID id , @RequestBody @NotNull OrderDetailsDto orderDetailsDtoToUpdate){
        return ResponseEntity.ok(service.updateOrderDetails(id,orderDetailsDtoToUpdate));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrderDetails(@PathVariable  UUID id){
        service.deleteOrderDetails(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDetailsDto> getOrderDetailsById(@PathVariable  UUID id){
        return ResponseEntity.ok(service.getOrderDetailsById(id));
    }

    @GetMapping("/AllOrderDetails")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<OrderDetailsDto> findAllCategories(Pageable pageable){
        return service.getAllOrderDetails(pageable);
    }

    //OTHER
    @GetMapping("/AllOrderDetails/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderDetailsDto> getAllOrderDetailsByOrderId(@PathVariable UUID orderId){
        return service.getAllOrderDetailsByOrderId(orderId);
    }


    //CART
    @GetMapping("/CartOrderDetails")
    public List<OrderDetailsDto> getAllCartOrderDetails(){
        return service.getAllCartOrderDetails();
    }





}