package com.example.eCommerce.controller;
import com.example.eCommerce.dto.CategoryDto;
import com.example.eCommerce.dto.OrderDetailsDto;
import com.example.eCommerce.dto.OrderDto;
import com.example.eCommerce.entity.Order;
import com.example.eCommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService service;

    @PostMapping("/new")
    public ResponseEntity<OrderDto> createOrder(@RequestBody @NotNull OrderDto EntityToCreate){
        if(EntityToCreate.getId()!=null){
            EntityToCreate.setId(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createOrder(EntityToCreate));
    }

    @PutMapping("/update")
    public ResponseEntity<OrderDto> updateOrder(@RequestBody @NotNull OrderDto updatedEntity){
        return ResponseEntity.ok(service.updateOrder(updatedEntity));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable @NotNull UUID id){
        service.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable UUID id){
        return ResponseEntity.ok(service.getOrderById(id));
    }


    @GetMapping("/AllOrder")
    public Page<OrderDto> findAllOrder(Pageable pageable){
        return service.getAllOrders(pageable);
    }

    //TODO: paginazione da implementare
    @GetMapping("/AllOrder/{state}")
    public List<OrderDto> findAllOrderByState(@PathVariable String state){
        return service.getAllOrdersByState(state);
    }

    //TODO: introdurre gestione ordini utente
    //1. order by state
    //in_elaborazione etc..


}
